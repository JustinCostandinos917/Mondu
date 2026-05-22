<?php
header("Content-Type: application/json; charset=UTF-8");
$koneksi = mysqli_connect("localhost", "root", "", "db_mondu");

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $username     = $_POST['username'];
    $password     = $_POST['password'];
    $nama_lengkap = $_POST['nama_lengkap'];
    $role         = $_POST['role']; // Harus: Admin / Guru / Wali Kelas / Siswa

    if(!empty($username) && !empty($password) && !empty($nama_lengkap) && !empty($role)) {
        // Cek apakah username sudah terpakai
        $cek = mysqli_query($koneksi, "SELECT * FROM tbl_user WHERE username='$username'");
        if(mysqli_num_rows($cek) > 0) {
            echo json_encode(["status" => "error", "message" => "Username sudah terdaftar!"]);
        } else {
            // Simpan user baru
            $query = "INSERT INTO tbl_user (username, password, nama_lengkap, role) VALUES ('$username', '$password', '$nama_lengkap', '$role')";
            if(mysqli_query($koneksi, $query)) {
                echo json_encode(["status" => "success", "message" => "Register berhasil!"]);
            } else {
                echo json_encode(["status" => "error", "message" => "Gagal menyimpan data"]);
            }
        }
    } else {
        echo json_encode(["status" => "error", "message" => "Semua kolom wajib diisi!"]);
    }
}
mysqli_close($koneksi);
?>