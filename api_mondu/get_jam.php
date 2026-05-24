<?php
header('Content-Type: application/json; charset=UTF-8');
include 'koneksi.php';

$id_kelas = isset($_GET['id_kelas']) ? trim($_GET['id_kelas']) : '';
$hari = isset($_GET['hari']) ? trim($_GET['hari']) : '';
$nuptk = isset($_GET['nuptk']) ? trim($_GET['nuptk']) : ''; 

// Validasi ketat: Jika parameter kosong, langsung stop kirim array kosong biar ga crash
if ($hari == '' || $nuptk == '' || $id_kelas == '') {
    echo json_encode([]); 
    exit;
}

// QUERY ANTI-BENTROK: Menampilkan jam pelajaran yang belum terpakai di kelas tersebut ATAU oleh guru tersebut
$query = "SELECT DISTINCT jp.id_jam, jp.jam_mulai, jp.jam_selesai 
          FROM jam_pelajaran jp
          LEFT JOIN jadwal j ON jp.id_jam = j.id_jam AND j.hari = '$hari' AND (j.id_kelas = '$id_kelas' OR j.nuptk = '$nuptk')
          WHERE j.id_jadwal IS NULL 
          AND jp.id_jam NOT IN (4, 9) -- Potong jam istirahat ke-4 dan ke-9
          ORDER BY jp.id_jam ASC";

$result = mysqli_query($conn, $query);

$data = array();
if ($result) {
    while ($row = mysqli_fetch_assoc($result)) {
        // Potong detik langsung di PHP agar rapi (:00 dibuang)
        $jam_m = isset($row['jam_mulai']) ? substr($row['jam_mulai'], 0, 5) : "00:00";
        $jam_s = isset($row['jam_selesai']) ? substr($row['jam_selesai'], 0, 5) : "00:00";
        
        // ISI KEY DI SINI DISAMAKAN DENGAN YANG DICARI ANDROID KAMU
        $data[] = [
            "id_jam" => $row['id_jam'],
            "jam_mulai" => $jam_m,
            "jam_selesai" => $jam_s
        ];
    }
}

echo json_encode($data);
?>