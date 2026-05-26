<?php
header("Content-Type: application/json; charset=UTF-8");

$koneksi = mysqli_connect("localhost", "root", "", "db_mondu");

// Cek koneksi
if (!$koneksi) {
    die(json_encode([
        "status" => "error",
        "message" => "Koneksi database gagal"
    ]));
}

// Pastikan request POST
if ($_SERVER['REQUEST_METHOD'] === 'POST') {

    $username = $_POST['username'] ?? '';
    $password = $_POST['password'] ?? '';

    // Query login user
    $query = "SELECT * FROM users 
              WHERE username='$username' 
              AND password='$password'";

    $result = mysqli_query($koneksi, $query);

    // Jika login berhasil
    if ($result && mysqli_num_rows($result) > 0) {

        $row = mysqli_fetch_assoc($result);
        $id_user = $row['id_user'];

        // Default awal (untuk mengantisipasi siswa/admin agar tidak undefined variable)
        $is_wali = false;
        $nuptk = ""; // Beri nilai default string kosong

        // Cari data guru berdasarkan id_user
        $queryGuru = mysqli_query(
            $koneksi,
            "SELECT nuptk FROM guru WHERE id_user='$id_user'"
        );

        // Jika user adalah guru
        if ($queryGuru && mysqli_num_rows($queryGuru) > 0) {

            $dataGuru = mysqli_fetch_assoc($queryGuru);
            $nuptk = $dataGuru['nuptk'];

            // Cek apakah nuptk jadi wali kelas
            $checkWali = mysqli_query(
                $koneksi,
                "SELECT id_kelas 
                 FROM kelas 
                 WHERE nuptk_walikelas='$nuptk'"
            );

            $is_wali = mysqli_num_rows($checkWali) > 0;
        }

        $is_pembina_eskul = false; // Default false

        // Cek apakah nuptk ini terdaftar di tabel ekskul
        if (!empty($nuptk)) {
            $checkEskul = mysqli_query(
                $koneksi,
                "SELECT id_eskul FROM eskul WHERE nuptk='$nuptk'"
            );
            $is_pembina_eskul = mysqli_num_rows($checkEskul) > 0;
        }

        // Response sukses
        echo json_encode([
            "status" => "success",
            "message" => "Login Berhasil!",
            "user" => [
                "id_user" => $row['id_user'],
                "nama_lengkap" => $row['nama_lengkap'],
                "role" => $row['role'],
                "is_walikelas" => $is_wali,
                "is_pembina_eskul" => $is_pembina_eskul, // <-- SEKARANG SUDAH DIBERI KOMA
                "nuptk" => $nuptk           // <-- SEKARANG AMAN UNTUK SEMUA ROLE
            ]
        ]);

    } else {
        // Login gagal
        echo json_encode([
            "status" => "error",
            "message" => "Username atau Password salah"
        ]);
    }

} else {
    echo json_encode([
        "status" => "error",
        "message" => "Harap gunakan metode POST"
    ]);
}

mysqli_close($koneksi);
?>