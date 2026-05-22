package com.example.mondu

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forgot_password)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // 1. Inisialisasi komponen GUI sesuai ID di XML baru
        val etResetEmail = findViewById<TextInputEditText>(R.id.etResetEmail)
        val btnResetSubmit = findViewById<Button>(R.id.btnResetSubmit)
        val tvBackToLoginLink = findViewById<TextView>(R.id.tvBackToLoginLink)

        // URL API untuk proses reset password (Sesuaikan IP jika pakai HP asli)
        val urlReset = "http://10.0.2.2/api_mondu/reset_password.php"

        btnResetSubmit.setOnClickListener {
            val email = etResetEmail.text.toString().trim()

            // Validasi 1: Kolom tidak boleh kosong
            if (email.isEmpty()) {
                Toast.makeText(this, "Alamat email wajib diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validasi 2: Memastikan format yang diketik adalah email asli (ada @ dan .com)
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Format email tidak valid!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 2. Eksekusi pengiriman data via Volley POST ke PHP
            val stringRequest = object : StringRequest(
                Request.Method.POST, urlReset,
                Response.Listener { response ->
                    try {
                        val jsonObject = JSONObject(response)
                        val status = jsonObject.getString("status")
                        val message = jsonObject.getString("message")

                        if (status == "success") {
                            // Munculkan pesan sukses bahwa email beneran terkirim
                            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                            finish() // Otomatis menutup halaman dan kembali ke Login
                        } else {
                            // Munculkan pesan gagal (misal: email tidak terdaftar)
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this, "Parsing Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener { error ->
                    Toast.makeText(this, "Koneksi Bermasalah: ${error.message}", Toast.LENGTH_LONG).show()
                }
            ) {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    // Kirim parameter bernama "email" ke PHP
                    params["email"] = email
                    return params
                }
            }

            // Memasukkan request ke antrean Volley agar langsung jalan berjalan
            Volley.newRequestQueue(this).add(stringRequest)
        }

        // Jika user klik "Kembali ke Halaman Masuk"
        tvBackToLoginLink.setOnClickListener {
            finish() // Menutup Activity ini untuk kembali ke halaman sebelumnya (Login)
        }
    }
}