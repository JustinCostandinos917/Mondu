<?php
header('Content-Type: application/json');
include "koneksi.php";

$id_user = $_GET['id_user'];
$role    = $_GET['role'];

$response = ["status" => "error", "data" => null];

if ($role === 'Siswa') {
    $stmt = $conn->prepare("SELECT nis, nisn, jenis_kelamin, tanggal_lahir, alamat, id_kelas FROM siswa WHERE id_user = ?");
    $stmt->bind_param("s", $id_user);
    $stmt->execute();
    $result = $stmt->get_result()->fetch_assoc();
    if ($result) {
        $response = ["status" => "success", "data" => $result];
    }
    $stmt->close();
} elseif ($role === 'Guru') {
    $stmt = $conn->prepare("SELECT nuptk FROM guru WHERE id_user = ?");
    $stmt->bind_param("s", $id_user);
    $stmt->execute();
    $result = $stmt->get_result()->fetch_assoc();
    if ($result) {
        $response = ["status" => "success", "data" => $result];
    }
    $stmt->close();
} else {
    $response = ["status" => "success", "data" => (object)[]];
}

echo json_encode($response);
$conn->close();
?>