<?php
header('Content-Type: application/json; charset=UTF-8');
include "koneksi.php";

$hari = isset($_GET['hari']) ? $_GET['hari'] : '';
$id_kelas = isset($_GET['id_kelas']) ? $_GET['id_kelas'] : '';
$nuptk = isset($_GET['nuptk']) ? $_GET['nuptk'] : ''; 

// Base Query (JOIN semua tabel)
$sql = "SELECT j.id_jadwal, j.id_mapel, j.id_kelas, j.hari, jp.jam_mulai, jp.jam_selesai, m.nama_mapel, k.nama_kelas, u.nama_lengkap AS nama_guru 
        FROM jadwal j
        JOIN jam_pelajaran jp ON j.id_jam = jp.id_jam
        JOIN mata_pelajaran m ON j.id_mapel = m.id_mapel
        JOIN kelas k ON j.id_kelas = k.id_kelas
        JOIN guru g ON m.nuptk_guru = g.nuptk
        JOIN users u ON g.id_user = u.id_user
        WHERE 1=1"; // 'WHERE 1=1' ini trik aja biar kita bisa sambung pakai 'AND' di bawahnya

// 1. FILTER BERDASARKAN HARI (Hanya jalan kalau Activity lain mengirim parameter hari)
if (!empty($hari)) {
    $sql .= " AND j.hari = '$hari'";
}

// 2. FILTER BERDASARKAN ROLE YANG MANGGIL
if (!empty($nuptk)) {
    $sql .= " AND m.nuptk_guru = '$nuptk'"; // Jika guru
} else if (!empty($id_kelas)) {
    $sql .= " AND j.id_kelas = '$id_kelas'"; // Jika siswa/activity lain
}

// Urutkan berdasarkan Senin-Minggu, lalu jam mulai
$sql .= " ORDER BY FIELD(j.hari, 'Senin', 'Selasa', 'Rabu', 'Kamis', 'Jumat', 'Sabtu', 'Minggu'), jp.jam_mulai ASC";

$result = $conn->query($sql);

if ($result) {
    $data = [];
    while($row = $result->fetch_assoc()) { 
        $jam_format = substr($row['jam_mulai'], 0, 5) . " - " . substr($row['jam_selesai'], 0, 5);
        
        $data[] = [
            "id_jadwal" => $row['id_jadwal'],
            "id_mapel" => $row['id_mapel'],  
            "id_kelas" => $row['id_kelas'],
            "hari" => $row['hari'], 
            "nama_mapel" => $row['nama_mapel'],
            "nama_kelas" => $row['nama_kelas'],
            "nama_guru" => $row['nama_guru'],
            
            // 1. Kirim "jam" gabungan untuk JadwalGuruFragment kamu
            "jam" => $jam_format, 
            
            // 2. Kirim kembali jam terpisah untuk Activity Admin / Activity lama kamu
            "jam_mulai" => $row['jam_mulai'],   // <-- TAMBAHKAN BARIS INI
            "jam_selesai" => $row['jam_selesai'] // <-- TAMBAHKAN BARIS INI
        ]; 
    }
    echo json_encode($data);
} else {
    echo json_encode(["error" => "Query gagal: " . $conn->error]);
}
?>