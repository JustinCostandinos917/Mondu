<?php
header('Content-Type: application/json');
include "koneksi.php";

$sql = "SELECT m.id_mapel, m.nama_mapel, u.nama_lengkap AS nama_guru, m.nuptk_guru AS nuptk 
        FROM mata_pelajaran m 
        LEFT JOIN guru g ON m.nuptk_guru = g.nuptk 
        LEFT JOIN users u ON g.id_user = u.id_user";

$result = $conn->query($sql);
$data = [];

if ($result) {
    while($row = $result->fetch_assoc()) {
        $data[] = $row;
    }
}

echo json_encode($data);
?>