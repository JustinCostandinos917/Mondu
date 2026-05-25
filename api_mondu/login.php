<?php
header("Content-Type: application/json; charset=UTF-8");
include "koneksi.php";

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $username = $_POST['username'];
    $password = $_POST['password'];

    if(!empty($username) && !empty($password)) {
        // Use prepared statements to prevent SQL injection
        $stmt = $conn->prepare("SELECT * FROM users WHERE username=? AND password=?");
        $stmt->bind_param("ss", $username, $password);
        $stmt->execute();
        $result = $stmt->get_result();

        if($result->num_rows > 0) {
            $row = $result->fetch_assoc();
            $id_user = $row['id_user'];
            $role = $row['role'];

            $user_data = [
                "id_user" => $id_user,
                "username" => $row['username'],
                "nama_lengkap" => $row['nama_lengkap'],
                "role" => $role
            ];

            // If user is a student, fetch their class ID
            if ($role === 'Siswa') {
                $stmt_siswa = $conn->prepare("SELECT id_kelas FROM siswa WHERE id_user=?");
                $stmt_siswa->bind_param("s", $id_user);
                $stmt_siswa->execute();
                $res_siswa = $stmt_siswa->get_result();
                if ($res_siswa->num_rows > 0) {
                    $row_siswa = $res_siswa->fetch_assoc();
                    $user_data["id_kelas"] = $row_siswa['id_kelas'];
                }
                $stmt_siswa->close();
            }

            echo json_encode([
                "status" => "success",
                "message" => "Login Berhasil!",
                "user" => $user_data
            ]);
        } else {
            echo json_encode(["status" => "error", "message" => "Username atau Password salah!"]);
        }
        $stmt->close();
    } else {
        echo json_encode(["status" => "error", "message" => "Kolom login kosong!"]);
    }
}
$conn->close();
?>
