<?php
header('Content-Type: application/json; charset=UTF-8');
include "koneksi.php";

$nuptk = isset($_GET['nuptk']) ? $_GET['nuptk'] : '';

if (empty($nuptk)) {
    echo json_encode(["error" => "NUPTK tidak ditemukan"]);
    exit;
}

// Query untuk mengambil daftar kelas & mapel unik yang diajar oleh guru tersebut
// Kita menggunakan DISTINCT agar kelas & mapel yang sama tidak muncul berkali-kali
$sql = "SELECT DISTINCT j.id_mapel, j.id_kelas, m.nama_mapel, k.nama_kelas 
        FROM jadwal j
        JOIN mata_pelajaran m ON j.id_mapel = m.id_mapel
        JOIN kelas k ON j.id_kelas = k.id_kelas
        WHERE m.nuptk_guru = '$nuptk'";

$result = $conn->query($sql);

if ($result) {
    $data = [];
    while($row = $result->fetch_assoc()) { 
        $data[] = [
            "id_mapel" => $row['id_mapel'],
            "id_kelas" => $row['id_kelas'],
            "nama_mapel" => $row['nama_mapel'],
            "nama_kelas" => $row['nama_kelas']
        ]; 
    }
    echo json_encode($data);
} else {
    echo json_encode(["error" => "Query gagal: " . $conn->error]);
}
?>