<?php

header("Content-Type: application/json");

include "koneksi.php";

$nis = $_GET['nis'];

$get = mysqli_query(
    $conn,
    "SELECT id_user FROM siswa WHERE nis='$nis'"
);

$data = mysqli_fetch_assoc($get);

$id_user = $data['id_user'];

$query = "DELETE FROM users WHERE id_user='$id_user'";

if (mysqli_query($conn, $query)) {

    echo json_encode([
        "success" => true,
        "message" => "Siswa berhasil dihapus"
    ]);

} else {

    echo json_encode([
        "success" => false,
        "message" => mysqli_error($conn)
    ]);
}