<?php
header('Content-Type: application/json');
include "koneksi.php";

// Memastikan bahwa yang masuk ke file ini adalah request POST (kiriman data form)
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $id_user      = $_POST['id_user'];
    $username     = $_POST['username'];
    $nama_lengkap = $_POST['nama_lengkap'];
    $email        = $_POST['email'];
    $role         = $_POST['role'];

    // Kita pakai Database Transaction biar kalau salah satu tabel gagal, semuanya dibatalkan
    $conn->begin_transaction();

    try {
        // [A] UPDATE DATA UTAMA DI TABEL USERS
        $stmt_user = $conn->prepare("UPDATE users SET username = ?, nama_lengkap = ?, email = ? WHERE id_user = ?");
        $stmt_user->bind_param("ssss", $username, $nama_lengkap, $email, $id_user);
        $stmt_user->execute();
        $stmt_user->close();

        // [B] UPDATE DATA SEKUNDER BERDASARKAN ROLE
        if ($role === 'Siswa') {
            $nis           = $_POST['nis'];
            $nisn          = $_POST['nisn'];
            $jenis_kelamin = $_POST['jenis_kelamin'];
            $tanggal_lahir = $_POST['tanggal_lahir'];
            $alamat        = $_POST['alamat'];
            $id_kelas      = $_POST['id_kelas'];

            $stmt_siswa = $conn->prepare("UPDATE siswa SET nis = ?, nisn = ?, jenis_kelamin = ?, tanggal_lahir = ?, alamat = ?, id_kelas = ? WHERE id_user = ?");
            $stmt_siswa->bind_param("sssssss", $nis, $nisn, $jenis_kelamin, $tanggal_lahir, $alamat, $id_kelas, $id_user);
            $stmt_siswa->execute();
            $stmt_siswa->close();

        } elseif ($role === 'Guru') {
            // Menangkap parameter NUPTK dari Android
            $nuptk = $_POST['nuptk']; 

            // Melakukan update ke tabel guru
            $stmt_guru = $conn->prepare("UPDATE guru SET nuptk = ? WHERE id_user = ?");
            $stmt_guru->bind_param("ss", $nuptk, $id_user);
            $stmt_guru->execute();
            $stmt_guru->close();
        }

        // Jika semua query UPDATE di atas sukses tanpa eror, simpan permanen ke MySQL
        $conn->commit();
        echo json_encode(["status" => "success", "message" => "Data user sukses diperbarui!"]);

    } catch (Exception $e) {
        // Jika ada satu saja yang eror (misal salah ketik nama kolom), batalkan semua perubahan
        $conn->rollback();
        echo json_encode(["status" => "error", "message" => "Gagal memperbarui data: " . $e->getMessage()]);
    }
} else {
    echo json_encode(["status" => "error", "message" => "Metode request tidak valid!"]);
}
$conn->close();
?>