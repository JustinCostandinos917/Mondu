<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
header('Content-Type: application/json');
include "koneksi.php";

try {
    if (isset($_GET['id_user']) && !empty($_GET['id_user'])) {
        $id_user = $_GET['id_user'];

        // Find NIS
        $stmt_nis = $conn->prepare("SELECT nis FROM siswa WHERE id_user = ?");
        if (!$stmt_nis) {
            throw new Exception("Prepare failed (nis): " . $conn->error);
        }
        $stmt_nis->bind_param("s", $id_user);
        $stmt_nis->execute();
        $res_nis = $stmt_nis->get_result();

        if ($res_nis->num_rows > 0) {
            $row_nis = $res_nis->fetch_assoc();
            $nis = $row_nis['nis'];

            $query = "SELECT a.*, m.nama_mapel, j.hari, j.jam_mulai, j.jam_selesai
                      FROM absensi a
                      JOIN jadwal j ON a.id_jadwal = j.id_jadwal
                      JOIN mapel m ON j.id_mapel = m.id_mapel
                      WHERE a.nis = ?
                      ORDER BY a.tanggal DESC";

            $stmt = $conn->prepare($query);
            if (!$stmt) {
                throw new Exception("Prepare failed (absensi): " . $conn->error);
            }
            $stmt->bind_param("s", $nis);
            $stmt->execute();
            $result = $stmt->get_result();

            $data = [];
            while ($row = $result->fetch_assoc()) {
                $data[] = $row;
            }

            echo json_encode(["status" => "success", "data" => $data]);
            $stmt->close();
        } else {
            echo json_encode(["status" => "error", "message" => "Data siswa tidak ditemukan"]);
        }
        $stmt_nis->close();
    } else {
        echo json_encode(["status" => "error", "message" => "Parameter id_user tidak valid"]);
    }
} catch (Exception $e) {
    echo json_encode(["status" => "error", "message" => $e->getMessage()]);
}

$conn->close();
?>
