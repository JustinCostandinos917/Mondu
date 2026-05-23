<?php
header('Content-Type: application/json');
include "koneksi.php";

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $id_kelas = $_POST['id_kelas'];
    $nuptk    = $_POST['nuptk'];

    // 1. Cek apakah guru ini sudah jadi wali kelas di kelas lain
    $check = $conn->prepare("SELECT id_kelas FROM kelas WHERE nuptk_walikelas = ? AND id_kelas != ?");
    $check->bind_param("ss", $nuptk, $id_kelas);
    $check->execute();
    $res = $check->get_result();

    if ($res->num_rows > 0) {
        echo json_encode(["status" => "error", "message" => "Guru ini sudah jadi wali kelas di kelas lain!"]);
    } else {
        // 2. Kalau aman, lanjut update
        $stmt = $conn->prepare("UPDATE kelas SET nuptk_walikelas = ? WHERE id_kelas = ?");
        $stmt->bind_param("ss", $nuptk, $id_kelas);
        
        if ($stmt->execute()) {
            echo json_encode(["status" => "success", "message" => "Wali kelas berhasil diupdate!"]);
        } else {
            echo json_encode(["status" => "error", "message" => "Gagal update: " . $conn->error]);
        }
    }
}
$conn->close();
?>