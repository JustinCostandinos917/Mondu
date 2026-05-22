<?php
header("Content-Type: application/json; charset=UTF-8");

$host     = "localhost";
$username = "root";
$password = "";
$database = "db_mondu";

$koneksi = mysqli_connect($host, $username, $password, $database);

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    // Menangkap data yang dikirim dari Android Studio
    $id_user = $_POST['id_user'];
    $judul   = $_POST['judul'];
    $konten  = $_POST['konten'];
    $tanggal = date('Y-m-d'); // Tanggal otomatis hari ini

    if (!empty($id_user) && !empty($judul) && !empty($konten)) {
        // Query memasukkan data dengan id_user pengirim asli
        $query = "INSERT INTO tbl_pengumuman (id_user, judul, konten, tanggal_post) 
                  VALUES ('$id_user', '$judul', '$konten', '$tanggal')";
        
        if (mysqli_query($koneksi, $query)) {
            echo json_encode(["status" => "success", "message" => "Pengumuman berhasil disimpan"]);
        } else {
            echo json_encode(["status" => "error", "message" => "Gagal eksekusi database"]);
        }
    } else {
        echo json_encode(["status" => "error", "message" => "Data input tidak lengkap"]);
    }
} else {
    echo json_encode(["status" => "error", "message" => "Metode request harus POST"]);
}

mysqli_close($koneksi);
?>