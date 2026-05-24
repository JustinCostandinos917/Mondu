<?php
include 'koneksi.php'; // Menggunakan koneksi dari file pusat

// Menerima data dari POST request Android
$id_jadwal = isset($_POST['id_jadwal']) ? $_POST['id_jadwal'] : '';
$id_mapel = isset($_POST['id_mapel']) ? $_POST['id_mapel'] : '';
$id_jam = isset($_POST['id_jam']) ? $_POST['id_jam'] : '';

// Validasi data
if (!empty($id_jadwal) && !empty($id_mapel) && !empty($id_jam)) {
    
    // Query update
    $sql = "UPDATE jadwal SET id_mapel = '$id_mapel', id_jam = '$id_jam' WHERE id_jadwal = '$id_jadwal'";

    if (mysqli_query($conn, $sql)) {
        echo "Success";
    } else {
        echo "Error: " . mysqli_error($conn);
    }
} else {
    echo "Error: Data tidak lengkap";
}
?>