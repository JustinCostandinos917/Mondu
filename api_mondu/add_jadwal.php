<?php
header('Content-Type: application/json');
include "koneksi.php";

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $id_kelas    = $_POST['id_kelas'];
    $id_mapel    = $_POST['id_mapel'];
    $nuptk     = $_POST['nuptk']; // Tangkap parameter nuptk baru
    $hari        = $_POST['hari'];
    $jam_mulai   = $_POST['jam_mulai']; 
    $jam_selesai = $_POST['jam_selesai']; 

    // 1. GENERATE ID JADWAL CUSTOM (JDW001, JDW002, dst.)
    $query_id = "SELECT MAX(CAST(REPLACE(id_jadwal, 'JDW', '') AS UNSIGNED)) as max_id FROM jadwal";
    $result_id = $conn->query($query_id);
    
    $next_id_num = 1;
    if ($result_id && $row_id = $result_id->fetch_assoc()) {
        if ($row_id['max_id'] !== null) {
            $next_id_num = $row_id['max_id'] + 1;
        }
    }
    $new_id_jadwal = "JDW" . str_pad($next_id_num, 3, "0", STR_PAD_LEFT);

    // 2. INSERT DATA (Ditambahkan field nuptk dan tipe datanya "s")
    $stmt = $conn->prepare("INSERT INTO jadwal (id_jadwal, id_kelas, id_mapel, nuptk, hari, jam_mulai, jam_selesai) VALUES (?, ?, ?, ?, ?, ?, ?)");
    $stmt->bind_param("sssssss", $new_id_jadwal, $id_kelas, $id_mapel, $nuptk, $hari, $jam_mulai, $jam_selesai);
    
    if ($stmt->execute()) {
        echo json_encode(["status" => "success", "message" => "Jadwal berhasil disimpan dengan ID: " . $new_id_jadwal]);
    } else {
        echo json_encode(["status" => "error", "message" => "Gagal menyimpan jadwal: " . $conn->error]);
    }
    $stmt->close();
}
$conn->close();
?>