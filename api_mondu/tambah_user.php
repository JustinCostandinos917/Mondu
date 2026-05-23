<?php
header('Content-Type: application/json');
include "koneksi.php";

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $username     = $_POST['username'];
    $nama_lengkap = $_POST['nama_lengkap'];
    $email        = $_POST['email'];
    $password     = $_POST['password']; 
    $role         = $_POST['role'];
    
    // 1. OTOMATIS GENERATE ID CUSTOM (USR001, USR002, dst.)
    $query_id = "SELECT MAX(CAST(REPLACE(id_user, 'USR', '') AS UNSIGNED)) as max_id FROM users";
    $result_id = $conn->query($query_id);
    
    $next_id_num = 1;
    if ($result_id && $row_id = $result_id->fetch_assoc()) {
        if ($row_id['max_id'] !== null) {
            $next_id_num = $row_id['max_id'] + 1;
        }
    }
    $new_id_user = "USR" . str_pad($next_id_num, 3, "0", STR_PAD_LEFT);
    
    // 2. PROSES INSERT MULTI-TABEL DENGAN TRANSAKSI
    $conn->begin_transaction();

    try {
        // [A] Insert ke tabel users
        $stmt_user = $conn->prepare("INSERT INTO users (id_user, username, email, password, nama_lengkap, role) VALUES (?, ?, ?, ?, ?, ?)");
        $stmt_user->bind_param("ssssss", $new_id_user, $username, $email, $password, $nama_lengkap, $role);
        $stmt_user->execute();
        $stmt_user->close();

        // [B] Cek Role untuk data sekunder
        if ($role === 'Siswa') {
            $nis           = $_POST['nis'];
            $nisn          = $_POST['nisn'];
            $jenis_kelamin = $_POST['jenis_kelamin'];
            $tanggal_lahir = $_POST['tanggal_lahir'];
            $alamat        = $_POST['alamat'];
            $id_kelas      = $_POST['id_kelas']; // ID Hasil kiriman dinamis Android (KLS001 - KLS006)
            
            $stmt_siswa = $conn->prepare("INSERT INTO siswa (nis, id_user, nisn, jenis_kelamin, tanggal_lahir, alamat, id_kelas) VALUES (?, ?, ?, ?, ?, ?, ?)");
            $stmt_siswa->bind_param("sssssss", $nis, $new_id_user, $nisn, $jenis_kelamin, $tanggal_lahir, $alamat, $id_kelas);
            $stmt_siswa->execute();
            $stmt_siswa->close();
            
        } elseif ($role === 'Guru') {
            $nuptk = $_POST['nuptk'];
            
            $stmt_guru = $conn->prepare("INSERT INTO guru (id_user, nuptk) VALUES (?, ?)");
            $stmt_guru->bind_param("ss", $new_id_user, $nuptk);
            $stmt_guru->execute();
            $stmt_guru->close();
        }

        // Jika semua sukses, simpan permanen
        $conn->commit();
        echo json_encode(["status" => "success", "message" => "User baru berhasil disimpan dengan ID: " . $new_id_user]);

    } catch (Exception $e) {
        $conn->rollback(); // Batalkan semua jika ada yang gagal
        echo json_encode(["status" => "error", "message" => "Gagal menyimpan data: " . $e->getMessage()]);
    }
} else {
    echo json_encode(["status" => "error", "message" => "Metode request tidak valid!"]);
}
$conn->close();
?>