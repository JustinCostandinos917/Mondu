<?php

header("Content-Type: application/json");

include "koneksi.php";

$data = json_decode(file_get_contents("php://input"));

$nis = $data->nis;
$nama = $data->nama;
$username = $data->username;
$password = $data->password;
$email = $data->email;
$jenis_kelamin = $data->jenis_kelamin;
$id_kelas = $data->id_kelas;

$id_user = "USR" . substr($nis, -3);

$query_user = "
INSERT INTO users
(id_user, nama, email, username, password, role)
VALUES
('$id_user', '$nama', '$email', '$username', '$password', 'siswa')
";

$query_siswa = "
INSERT INTO siswa
(nis, id_user, jenis_kelamin, id_kelas)
VALUES
('$nis', '$id_user', '$jenis_kelamin', '$id_kelas')
";

if (
    mysqli_query($conn, $query_user) &&
    mysqli_query($conn, $query_siswa)
) {

    echo json_encode([
        "success" => true,
        "message" => "Siswa berhasil ditambahkan"
    ]);

} else {

    echo json_encode([
        "success" => false,
        "message" => mysqli_error($conn)
    ]);
}