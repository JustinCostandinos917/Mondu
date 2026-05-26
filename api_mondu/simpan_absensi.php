<?php
header("Content-Type: application/json");
include "koneksi.php";

$json_data = file_get_contents("php://input");
$data = json_decode($json_data, true);

if (!$data) {
    echo json_encode(["status" => "error", "message" => "Data tidak valid"]);
    exit;
}

$id_jadwal = $data['id_jadwal'];
$tanggal = date('Y-m-d');
$absensi_list = $data['absensi']; // array berisi nis dan status

$koneksi->begin_transaction();

try {
    foreach ($absensi_list as $item) {
        $nis = $item['nis'];
        $status = $item['status']; // 'Hadir', 'Sakit', 'Izin', 'Alpha'

        // 1. Simpan ke tabel absensi_harian (History)
        $stmt = $koneksi->prepare("INSERT INTO absensi_harian (id_jadwal, nis, status, tanggal) VALUES (?, ?, ?, ?)");
        $stmt->bind_param("ssss", $id_jadwal, $nis, $status, $tanggal);
        $stmt->execute();

        // 2. Update tabel 'absensi' (Rekapitulasi) jika bukan 'Hadir'
        if ($status != 'Hadir') {
            $kolom = strtolower($status); // 'sakit', 'izin', atau 'alpha'
            
            // Cek apakah data siswa sudah ada di tabel rekap
            $cekRekap = $koneksi->prepare("SELECT id_absensi FROM absensi WHERE nis = ?");
            $cekRekap->bind_param("s", $nis);
            $cekRekap->execute();
            
            if ($cekRekap->get_result()->num_rows > 0) {
                // Update jumlah
                $update = $koneksi->prepare("UPDATE absensi SET $kolom = $kolom + 1 WHERE nis = ?");
                $update->bind_param("s", $nis);
                $update->execute();
            } else {
                // Buat data baru jika belum ada
                $insert = $koneksi->prepare("INSERT INTO absensi (nis, $kolom) VALUES (?, 1)");
                $insert->bind_param("s", $nis);
                $insert->execute();
            }
        }
    }

    $koneksi->commit();
    echo json_encode(["status" => "success", "message" => "Absensi berhasil disimpan dan rekap diupdate"]);

} catch (Exception $e) {
    $koneksi->rollback();
    echo json_encode(["status" => "error", "message" => "Gagal: " . $e->getMessage()]);
}
?>