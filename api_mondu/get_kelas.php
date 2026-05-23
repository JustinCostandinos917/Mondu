<?php
header('Content-Type: application/json');
include "koneksi.php";

$sql = "SELECT id_kelas, nama_kelas FROM kelas ORDER BY id_kelas ASC";
$result = $conn->query($sql);

$kelas_list = array();

if ($result && $result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        $kelas_list[] = $row;
    }
}

echo json_encode($kelas_list);
$conn->close();
?>