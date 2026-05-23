<?php
header('Content-Type: application/json');
include "koneksi.php";

// Query dengan LEFT JOIN untuk mengambil nama wali kelas
$sql = "SELECT k.id_kelas, k.nama_kelas, u.nama_lengkap as nama_wali 
        FROM kelas k 
        LEFT JOIN guru g ON k.nuptk_walikelas = g.nuptk 
        LEFT JOIN users u ON g.id_user = u.id_user 
        ORDER BY k.id_kelas ASC";

$result = $conn->query($sql);
$kelas_list = array();

if ($result && $result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        if(empty($row['nama_wali'])) {
            $row['nama_wali'] = "-";
        }
        $kelas_list[] = $row;
    }
}

echo json_encode($kelas_list);
$conn->close();
?>