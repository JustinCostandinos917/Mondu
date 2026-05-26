<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
header('Content-Type: application/json');
include "koneksi.php";

try {
    if (isset($_GET['id_user']) && !empty($_GET['id_user'])) {
        $id_user = $_GET['id_user'];

        // 1. Cari NIS Siswa berdasarkan id_user
        $stmt_nis = $conn->prepare("SELECT nis FROM siswa WHERE id_user = ?");
        if (!$stmt_nis) {
            throw new Exception("Gagal menyiapkan statement (nis): " . $conn->error);
        }
        $stmt_nis->bind_param("s", $id_user);
        $stmt_nis->execute();
        $res_nis = $stmt_nis->get_result();

        if ($res_nis->num_rows > 0) {
            $row_nis = $res_nis->fetch_assoc();
            $nis = $row_nis['nis'];

            // 2. Ambil data absensi dengan JOIN ke jadwal, mata_pelajaran, dan jam_pelajaran
            $query = "SELECT
                        a.id_absensi,
                        m.nama_mapel,
                        a.tanggal,
                        a.status,
                        jp.jam_mulai,
                        jp.jam_selesai
                      FROM absensi a
                      JOIN jadwal j ON a.id_jadwal = j.id_jadwal
                      JOIN mata_pelajaran m ON j.id_mapel = m.id_mapel
                      JOIN jam_pelajaran jp ON j.id_jam = jp.id_jam
                      WHERE a.nis = ?
                      ORDER BY a.tanggal DESC, jp.jam_mulai DESC";

            $stmt = $conn->prepare($query);
            if (!$stmt) {
                throw new Exception("Gagal menyiapkan statement (absensi): " . $conn->error);
            }
            $stmt->bind_param("s", $nis);
            $stmt->execute();
            $result = $stmt->get_result();

            $data = [];
            while ($row = $result->fetch_assoc()) {
                // Pastikan format jam hanya HH:mm agar rapi di aplikasi
                $row['jam_mulai'] = substr($row['jam_mulai'], 0, 5);
                $row['jam_selesai'] = substr($row['jam_selesai'], 0, 5);
                $data[] = $row;
            }

            echo json_encode([
                "status" => "success",
                "data" => $data
            ]);

            $stmt->close();
        } else {
            echo json_encode(["status" => "error", "message" => "Data siswa tidak ditemukan untuk user ini"]);
        }
        $stmt_nis->close();
    } else {
        echo json_encode(["status" => "error", "message" => "Parameter id_user tidak ditemukan"]);
    }
} catch (Exception $e) {
    echo json_encode(["status" => "error", "message" => $e->getMessage()]);
}

$conn->close();
?>
