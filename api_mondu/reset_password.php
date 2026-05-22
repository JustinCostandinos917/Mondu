<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);

header("Content-Type: application/json; charset=UTF-8");

// 1. Import Library PHPMailer Manual
require 'PHPMailer/Exception.php';
require 'PHPMailer/PHPMailer.php';
require 'PHPMailer/SMTP.php';

use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    // 2. Langsung tangkap apa yang diketik user di Android Studio
    $email = $_POST['email'];

    if (!empty($email)) {
        // Konfigurasi Pengiriman Email via PHPMailer
        $mail = new PHPMailer(true);

        try {
            // Server Settings
            $mail->isSMTP();
            $mail->Host       = 'smtp.gmail.com';                     
            $mail->SMTPAuth   = true;                                 
            $mail->Username   = 'blobbleb58@gmail.com';               
            $mail->Password   = 'xiagubmkyzaysjnd';                
            $mail->SMTPSecure = PHPMailer::ENCRYPTION_STARTTLS;       
            $mail->Port       = 587;                                  

            // Penerima & Pengirim (Nama penerima kita buat default 'User Mondu')
            $mail->setFrom('blobbleb58@gmail.com', 'e-Rapor Mondu');
            $mail->addAddress($email, 'User Mondu'); // Ditujukan langsung ke email inputan

            // Konten Email Berupa HTML
            $mail->isHTML(true);
            $mail->Subject = 'Instruksi Reset Kata Sandi Akun Mondu';
            
            $mail->Body    = "
                <h3>Halo, Pengguna Mondu!</h3>
                <p>Kami menerima permintaan untuk merestart kata sandi akun Anda di aplikasi <b>Mondu (Monitoring Education)</b>.</p>
                <p>Silakan klik link di bawah ini untuk mengatur ulang kata sandi Anda:</p>
                <p><a href='http://192.168.56.1/api_mondu/halaman_reset.php?email=$email' style='background:#1E3A8A; color:white; padding:10px 20px; text-decoration:none; border-radius:5px;'>Reset Password Saya</a></p>
                <br>
                <p><i>Jika Anda tidak merasa melakukan permintaan ini, abaikan saja email ini.</i></p>
            ";

            // Eksekusi kirim email beneran
            $mail->send();

            echo json_encode([
                "status" => "success", 
                "message" => "Link reset kata sandi beneran terkirim ke " . $email
            ]);

        } catch (Exception $e) {
            echo json_encode([
                "status" => "error", 
                "message" => "Gagal mengirim email. Mailer Error: {$mail->ErrorInfo}"
            ]);
        }

    } else {
        echo json_encode([
            "status" => "error", 
            "message" => "Kolom email kosong!"
        ]);
    }
} else {
    echo json_encode([
        "status" => "error",
        "message" => "Metode akses tidak diizinkan! Gunakan POST lewat aplikasi Android Mondu."
    ]);
}
?>