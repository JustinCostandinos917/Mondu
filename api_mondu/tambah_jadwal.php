<?php
include "koneksi.php";

$query_id = "SELECT id_jadwal FROM jadwal ORDER BY id_jadwal DESC LIMIT 1";
$result_id = mysqli_query($conn, $query_id);
$data_id = mysqli_fetch_assoc($result_id);

if ($data_id) {
    // Ambil angka dari "JDW001" -> 001
    $last_num = (int)substr($data_id['id_jadwal'], 3);
    $new_num = $last_num + 1;
} else {
    $new_num = 1;
}

// Format kembali jadi "JDW001"
$new_id = "JDW" . str_pad($new_num, 3, "0", STR_PAD_LEFT);

$id_kelas = $_POST['id_kelas'];
$id_mapel = $_POST['id_mapel'];
$id_jam   = $_POST['id_jam'];
$hari     = $_POST['hari'];
$nuptk    = $_POST['nuptk']; // Pastikan kamu punya data NUPTK guru

$sql = "INSERT INTO jadwal (id_jadwal, id_kelas, id_mapel, id_jam, hari, nuptk) 
        VALUES ('$new_id', '$id_kelas', '$id_mapel', '$id_jam', '$hari', '$nuptk')";

if (mysqli_query($conn, $sql)) {
    echo "Berhasil";
} else {
    echo "Gagal: " . mysqli_error($conn);
}
?>