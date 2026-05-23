<?php
header("Content-Type: application/json; charset=UTF-8");
$koneksi = mysqli_connect("localhost", "root", "", "db_mondu");

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $username = $_POST['username'];
    $password = $_POST['password'];

    if(!empty($username) && !empty($password)) {
        $query = "SELECT * FROM users WHERE username='$username' AND password='$password'";
        $result = mysqli_query($koneksi, $query);

        if(mysqli_num_rows($result) > 0) {
            $row = mysqli_fetch_assoc($result);
            echo json_encode([
                "status" => "success",
                "message" => "Login Berhasil!",
                "user" => [
                    "id_user" => $row['id_user'],
                    "username" => $row['username'],
                    "nama_lengkap" => $row['nama_lengkap'],
                    "role" => $row['role']
                ]
            ]);
        } else {
            echo json_encode(["status" => "error", "message" => "Username atau Password salah!"]);
        }
    } else {
        echo json_encode(["status" => "error", "message" => "Kolom login kosong!"]);
    }
}
mysqli_close($koneksi);
?>