<?php
header("Content-Type: application/json");
include "koneksi.php";

// 1. Ambil id_kelas dari URL (dikirim dari Android)
$id_kelas = isset($_GET['id_kelas']) ? $_GET['id_kelas'] : '';

if (empty($id_kelas)) {
    echo json_encode(["success" => false, "message" => "ID Kelas tidak ditemukan"]);
    exit;
}

// 2. Query dengan WHERE untuk memfilter siswa per kelas
$query = "
SELECT 
    siswa.nis, 
    users.nama_lengkap, 
    kelas.nama_kelas, 
    siswa.jenis_kelamin 
FROM siswa 
JOIN users ON siswa.id_user = users.id_user 
LEFT JOIN kelas ON siswa.id_kelas = kelas.id_kelas
WHERE siswa.id_kelas = '$id_kelas'
";

$result = mysqli_query($conn, $query);
$data = [];

while ($row = mysqli_fetch_assoc($result)) {
    $data[] = $row;
}

echo json_encode([
    "success" => true,
    "data" => $data
]);
?>