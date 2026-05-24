<?php
header('Content-Type: application/json');
include 'koneksi.php';

$id_kelas = isset($_GET['id_kelas']) ? $_GET['id_kelas'] : '';
$hari = isset($_GET['hari']) ? $_GET['hari'] : '';

$query = "SELECT jp.id_jam, jp.jam_mulai, jp.jam_selesai 
          FROM jam_pelajaran jp
          WHERE jp.id_jam NOT IN (
              SELECT id_jam 
              FROM jadwal 
              WHERE id_kelas = '$id_kelas' AND hari = '$hari'
          )
          AND jp.id_jam NOT IN (4, 9)
          ORDER BY jp.id_jam ASC";

$result = mysqli_query($conn, $query);

$data = array();
if ($result) {
    while ($row = mysqli_fetch_assoc($result)) {
        $data[] = $row;
    }
}

echo json_encode($data);
?>