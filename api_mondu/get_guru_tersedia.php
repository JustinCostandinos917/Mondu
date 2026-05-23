<?php
header('Content-Type: application/json');
include "koneksi.php";

// Kita butuh tahu kelas mana yang lagi diedit biar gurunya gak ilang dari pilihan
$id_kelas = isset($_GET['id_kelas']) ? $_GET['id_kelas'] : '';

$sql = "SELECT g.nuptk, u.nama_lengkap 
        FROM guru g 
        JOIN users u ON g.id_user = u.id_user 
        WHERE g.nuptk NOT IN (
            SELECT nuptk_walikelas 
            FROM kelas 
            WHERE nuptk_walikelas IS NOT NULL 
            AND nuptk_walikelas != ''
            AND id_kelas != '$id_kelas' -- Kecualikan kelas lain, tapi jangan kelas ini sendiri
        )
        ORDER BY u.nama_lengkap ASC";

$result = $conn->query($sql);
$guru_list = array();
while($row = $result->fetch_assoc()) {
    $guru_list[] = $row;
}

echo json_encode($guru_list);
$conn->close();
?>