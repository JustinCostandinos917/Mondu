<?php
header('Content-Type: application/json');
include "koneksi.php";

// Kita ambil g.nuptk bukan g.id_user
$sql = "SELECT g.nuptk, u.nama_lengkap 
        FROM guru g 
        JOIN users u ON g.id_user = u.id_user 
        WHERE u.role = 'guru'  
        ORDER BY u.nama_lengkap ASC";
$result = $conn->query($sql);

$guru_list = array();
if ($result && $result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        $guru_list[] = $row;
    }
}
echo json_encode($guru_list);
$conn->close();
?>