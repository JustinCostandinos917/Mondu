<?php
header('Content-Type: application/json; charset=utf-8');
// Koneksi ke database MySQL
$host = "localhost";
$user = "root";
$pass = "";
$db   = "db_mondu";

$conn = mysqli_connect($host, $user, $pass, $db);

// Memastikan request yang masuk berbentuk POST
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    
    // Menangkap data POST dari Android Studio
    $username     = $_POST['username'];
    $passwordLama = $_POST['password_lama'];
    $passwordBaru = $_POST['password_baru'];

    // 1. Cek apakah username dan password lama tersebut cocok di database
    $query_cek = "SELECT * FROM users WHERE username = '$username' AND password = '$passwordLama'";
    $result_cek = mysqli_query($conn, $query_cek);

    if (mysqli_num_rows($result_cek) > 0) {
        // 2. Jika cocok, lakukan update ke password baru
        $query_update = "UPDATE users SET password = '$passwordBaru' WHERE username = '$username'";
        
        if (mysqli_query($conn, $query_update)) {
            echo json_encode([
                "status"  => "success",
                "message" => "Password berhasil diperbarui!"
            ]);
        } else {
            echo json_encode([
                "status"  => "error",
                "message" => "Gagal memperbarui database."
            ]);
        }
    } else {
        // Jika password lama salah
        echo json_encode([
            "status"  => "error",
            "message" => "Password lama kamu salah!"
        ]);
    }
} else {
    echo json_encode([
        "status"  => "error",
        "message" => "Metode request tidak valid."
    ]);
}

mysqli_close($conn);
?>