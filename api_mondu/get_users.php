<?php
header('Content-Type: application/json');

include "koneksi.php";

if ($conn->connect_error) {
    echo json_encode(["status" => "error", "message" => "Koneksi gagal"]);
    exit();
}

// Ambil semua data user
$sql = "SELECT id_user, username, email, nama_lengkap, role, foto_profil FROM users ORDER BY CAST(REPLACE(id_user, 'USR', '') AS UNSIGNED) DESC";
$result = $conn->query($sql);

$users = array();
if ($result && $result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        $users[] = $row;
    }
}

// Kirim data ke Android
echo json_encode($users);
$conn->close();
?>