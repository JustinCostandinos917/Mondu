<?php
header('Content-Type: application/json');
include "koneksi.php";

$hari = isset($_GET['hari']) ? $_GET['hari'] : '';
$id_kelas = isset($_GET['id_kelas']) ? $_GET['id_kelas'] : '';

$sql = "SELECT j.id_jadwal, jp.jam_mulai, jp.jam_selesai, m.nama_mapel, u.nama_lengkap AS nama_guru 
        FROM jadwal j
        JOIN jam_pelajaran jp ON j.id_jam = jp.id_jam
        JOIN mata_pelajaran m ON j.id_mapel = m.id_mapel
        JOIN guru g ON j.nuptk = g.nuptk
        JOIN users u ON g.id_user = u.id_user
        WHERE j.hari = '$hari' AND j.id_kelas = '$id_kelas'
        ORDER BY jp.jam_mulai ASC";

$result = $conn->query($sql);

if ($result) {
    $data = [];
    while($row = $result->fetch_assoc()) { 
        $data[] = $row; 
    }
    echo json_encode($data);
} else {
    // Jika query gagal, kirim pesan error sebagai JSON, bukan HTML
    echo json_encode(["error" => "Query gagal: " . $conn->error]);
}
?>