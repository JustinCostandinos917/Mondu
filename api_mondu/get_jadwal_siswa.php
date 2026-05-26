<?php
// Set header to JSON
header('Content-Type: application/json');

// Error reporting for debugging
error_reporting(E_ALL);
ini_set('display_errors', 1);

include "koneksi.php";

try {
    // 1. Get Class ID (id_kelas)
    if (isset($_GET['id_kelas']) && !empty($_GET['id_kelas'])) {
        $id_kelas = $_GET['id_kelas'];
    } elseif (isset($_GET['id_user']) && !empty($_GET['id_user'])) {
        $id_user = $_GET['id_user'];
        $stmt_siswa = $conn->prepare("SELECT id_kelas FROM siswa WHERE id_user = ?");
        if (!$stmt_siswa) {
            throw new Exception("Database error (siswa): " . $conn->error);
        }
        $stmt_siswa->bind_param("s", $id_user);
        $stmt_siswa->execute();
        $res_siswa = $stmt_siswa->get_result();
        if ($res_siswa->num_rows > 0) {
            $row_siswa = $res_siswa->fetch_assoc();
            $id_kelas = $row_siswa['id_kelas'];
        } else {
            echo json_encode(["status" => "error", "message" => "Siswa belum terdaftar di kelas manapun"]);
            exit;
        }
        $stmt_siswa->close();
    } else {
        echo json_encode(["status" => "error", "message" => "Parameter tidak lengkap (id_kelas/id_user)"]);
        exit;
    }

    // 2. Get Today's Day in Indonesian
    $days = [
        'Monday' => 'Senin',
        'Tuesday' => 'Selasa',
        'Wednesday' => 'Rabu',
        'Thursday' => 'Kamis',
        'Friday' => 'Jumat',
        'Saturday' => 'Sabtu',
        'Sunday' => 'Minggu'
    ];
    $hari_indo = $days[date('l')];

    // 3. Query Jadwal (Join with Mapel and Guru)
    // Adjust JOINs based on your actual table names
    $query = "SELECT j.*, jm.jam_mulai, jm.jam_selesai, m.nama_mapel, u.nama_lengkap as nama_guru
          FROM jadwal j
          JOIN mata_pelajaran m ON j.id_mapel = m.id_mapel
          JOIN jam_pelajaran jm ON jm.id_jam = j.id_jam
          JOIN guru g ON m.nuptk_guru = g.nuptk
          JOIN users u ON g.id_user = u.id_user
          WHERE j.id_kelas = ? 
          ORDER BY FIELD(j.hari, 'Senin', 'Selasa', 'Rabu', 'Kamis', 'Jumat', 'Sabtu', 'Minggu'), jm.jam_mulai ASC";

    $stmt = $conn->prepare($query);
    if (!$stmt) {
        throw new Exception("Database error (jadwal): " . $conn->error);
    }

    // Hanya bind id_kelas saja
    $stmt->bind_param("s", $id_kelas); 
    $stmt->execute();
    $result = $stmt->get_result();

    $data = [];
    while ($row = $result->fetch_assoc()) {
        $data[] = $row;
    }

    echo json_encode([
        "status" => "success",
        "data" => $data,
        "debug_hari" => $hari_indo,
        "debug_kelas" => $id_kelas
    ]);

    $stmt->close();

} catch (Exception $e) {
    echo json_encode(["status" => "error", "message" => "Terjadi kesalahan: " . $e->getMessage()]);
}

$conn->close();
?>