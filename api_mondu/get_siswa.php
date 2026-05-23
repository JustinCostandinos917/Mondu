<?php

header("Content-Type: application/json");

include "koneksi.php";

$query = "
SELECT
    siswa.nis,
    users.nama,
    kelas.nama_kelas,
    siswa.jenis_kelamin
FROM siswa
JOIN users ON siswa.id_user = users.id_user
LEFT JOIN kelas ON siswa.id_kelas = kelas.id_kelas
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