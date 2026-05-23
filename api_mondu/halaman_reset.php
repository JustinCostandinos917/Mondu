<?php
session_start();

$koneksi = mysqli_connect("localhost", "root", "", "db_mondu");

// Tangkap email dari URL (?email=...)
if (isset($_GET['email'])) {
    $email = mysqli_real_escape_string($koneksi, $_GET['email']);
    
    // 🔥 CEK APURAKAH EMAIL TERDAFTAR DI DATABASE ATAU TIDAK
    $cek_email = mysqli_query($koneksi, "SELECT id_user FROM users WHERE email = '$email' LIMIT 1");
    
    if (mysqli_num_rows($cek_email) == 0) {
        // Jika hasil pencarian 0 baris, artinya email palsu / tidak terdaftar
        die("<div style='text-align:center; margin-top:50px; font-family:sans-serif;'>
                <h2>Akses Ditolak!</h2>
                <p>Email <b>" . htmlspecialchars($email) . "</b> tidak terdaftar di sistem aplikasi Mondu.</p>
             </div>");
    }
} else {
    die("Akses ilegal! Token atau Email tidak ditemukan.");
}

// Proses jika tombol "Perbarui Password" diklik
if (isset($_POST['submit'])) {
    $password_baru = $_POST['password_baru'];
    $konfirmasi_password = $_POST['konfirmasi_password'];

    if (!empty($password_baru) && !empty($konfirmasi_password)) {
        if ($password_baru === $konfirmasi_password) {
            
            // 🚀 LANGSUNG UPDATE PASSWORD DI DATABASE
            // Catatan: Jika di Login kamu pakai MD5, bungkus dengan md5($password_baru)
            $query_update = "UPDATE users SET password = '$password_baru' 
                             WHERE id_user = (SELECT id_user FROM users WHERE email = '$email' LIMIT 1)";
            $execute = mysqli_query($koneksi, $query_update);

            if ($execute) {
                echo "<script>
                        alert('Password berhasil diperbarui! Silakan kembali login di aplikasi Mondu.');
                        window.close();
                      </script>";
            } else {
                $error = "Gagal memperbarui database.";
            }
        } else {
            $error = "Konfirmasi password tidak cocok!";
        }
    } else {
        $error = "Semua kolom wajib diisi!";
    }
}
?>

<!DOCTYPE html>
<html lang="id">

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Reset Password - Mondu Portal</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <style>
  body {
    background-color: #F8FAFC;
  }

  .card-reset {
    max-width: 450px;
    margin: 80px auto;
    border-radius: 16px;
    border: 1px solid #E2E8F0;
  }

  .btn-primary {
    background-color: #1E3A8A;
    border: none;
  }

  .btn-primary:hover {
    background-color: #172554;
  }
  </style>
</head>

<body>

  <div class="container">
    <div class="card card-reset shadow-sm p-4 bg-white">
      <h3 class="text-center fw-bold text-dark mb-2">Password Baru</h3>
      <p class="text-center text-muted small mb-4">Masukkan password baru untuk akun dengan
        email:<br><strong><?php echo htmlspecialchars($email); ?></strong></p>

      <?php if(isset($error)): ?>
      <div class="alert alert-danger text-center small py-2"><?php echo $error; ?></div>
      <?php endif; ?>

      <form action="" method="POST">
        <div class="mb-3">
          <label class="form-label small fw-semibold text-secondary">Password Baru</label>
          <input type="password" name="password_baru" class="form-control" placeholder="Minimal 6 karakter" required>
        </div>
        <div class="mb-4">
          <label class="form-label small fw-semibold text-secondary">Konfirmasi Password Baru</label>
          <input type="password" name="konfirmasi_password" class="form-control" placeholder="Ulangi password baru"
            required>
        </div>
        <button type="submit" name="submit" class="btn btn-primary w-100 py-2 fw-bold rounded-3">PERBARUI
          PASSWORD</button>
      </form>
    </div>
  </div>

</body>

</html>