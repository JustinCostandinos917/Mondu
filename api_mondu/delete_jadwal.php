<?php
include "koneksi.php";

// Ambil id_jadwal dari parameter URL
$id_jadwal = isset($_GET['id_jadwal']) ? $_GET['id_jadwal'] : '';

if (!empty($id_jadwal)) {
    // Query untuk menghapus data berdasarkan ID
    $sql = "DELETE FROM jadwal WHERE id_jadwal = '$id_jadwal'";

    if ($conn->query($sql) === TRUE) {
        // Berhasil dihapus
        echo json_encode(["status" => "success", "message" => "Data berhasil dihapus"]);
    } else {
        // Gagal dihapus
        echo json_encode(["status" => "error", "message" => "Gagal menghapus: " . $conn->error]);
    }
} else {
    echo json_encode(["status" => "error", "message" => "ID tidak ditemukan"]);
}
?>