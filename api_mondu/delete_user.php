<?php
header('Content-Type: application/json');
include "koneksi.php";

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $id_user = $_POST['id_user'];

    $conn->begin_transaction();

    try {
        // 1. Hapus relasi di tabel anak terlebih dahulu (Siswa dan Guru)
        $stmt_siswa = $conn->prepare("DELETE FROM siswa WHERE id_user = ?");
        $stmt_siswa->bind_param("s", $id_user);
        $stmt_siswa->execute();
        $stmt_siswa->close();

        $stmt_guru = $conn->prepare("DELETE FROM guru WHERE id_user = ?");
        $stmt_guru->bind_param("s", $id_user);
        $stmt_guru->execute();
        $stmt_guru->close();

        // 2. Hapus data utama di tabel users
        $stmt_user = $conn->prepare("DELETE FROM users WHERE id_user = ?");
        $stmt_user->bind_param("s", $id_user);
        $stmt_user->execute();
        $stmt_user->close();

        $conn->commit();
        echo json_encode(["status" => "success", "message" => "User berhasil dihapus secara permanen!"]);

    } catch (Exception $e) {
        $conn->rollback();
        echo json_encode(["status" => "error", "message" => "Gagal menghapus data: " . $e->getMessage()]);
    }
} else {
    echo json_encode(["status" => "error", "message" => "Metode request tidak valid!"]);
}
$conn->close();
?>