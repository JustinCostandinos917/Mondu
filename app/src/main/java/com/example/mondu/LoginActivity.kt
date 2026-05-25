package com.example.mondu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val etUsername = findViewById<TextInputEditText>(R.id.etUsername)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvRegisterLink = findViewById<TextView>(R.id.tvRegisterLink)
        val tvforgotPasswordLink = findViewById<TextView>(R.id.tvForgotPasswordLink)

        // URL backend API PHP (Ganti localhost ke 10.0.2.2 jika pakai emulator bawaan)
        val urlLogin = "http://10.0.2.2/api_mondu/login.php"

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username dan Kata Sandi wajib diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Eksekusi data login lewat Volley
            val stringRequest = object : StringRequest(Request.Method.POST, urlLogin,
                Response.Listener { response ->
                    try {
                        Log.d("RESPON_SERVER", response);
                        val jsonObject = JSONObject(response)
                        val status = jsonObject.getString("status")
                        val message = jsonObject.getString("message")

                        if (status == "success") {
                            val dataUser = jsonObject.getJSONObject("user")
                            val idUser = dataUser.getString("id_user")
                            val role = dataUser.getString("role")
                            val namaUser = dataUser.getString("nama_lengkap")
                            val isWali = dataUser.optBoolean("is_walikelas")
                            val nuptk = dataUser.optString("nuptk", "")

                            Toast.makeText(this, "Selamat datang, $namaUser!", Toast.LENGTH_SHORT).show()

                            val sharedPref = getSharedPreferences("MonduSession", Context.MODE_PRIVATE)
                            val editor = sharedPref.edit()

                            editor.putBoolean("isLoggedIn", true) // Tanda kalau user sudah login
                            editor.putString("id_user", idUser)
                            editor.putString("username", username)
                            editor.putString("nama_lengkap", namaUser)
                            editor.putString("role", role)
                            editor.putBoolean("is_walikelas", isWali)
                            editor.putString("nuptk", nuptk)

                            if (dataUser.has("id_kelas")) {
                                editor.putString("id_kelas", dataUser.getString("id_kelas"))
                            }

                            editor.apply()
                            // Alur pemisahan halaman berdasarkan Hak Akses/Role
                            if (role == "Siswa") {
                                // Arahkan ke dashboard siswa utama
                                startActivity(Intent(this, DashboardSiswaActivity::class.java))
                            } else if (role == "Guru") {
                                // Arahkan ke halaman input pengumuman atau dashboard guru
                                startActivity(Intent(this, DashboardGuruActivity::class.java))
                            } else if (role == "Admin") {
                                // Arahkan ke halaman input pengumuman atau dashboard guru
                                startActivity(Intent(this, DashboardAdminActivity::class.java))
                            }
                            finish() // Tutup halaman login
                        } else {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this, "Format Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener { error ->
                    Toast.makeText(this, "Koneksi Bermasalah: ${error.message}", Toast.LENGTH_LONG).show()
                }
            ) {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["username"] = username
                    params["password"] = password
                    return params
                }
            }

            Volley.newRequestQueue(this).add(stringRequest)
        }

        // Navigasi pindah ke halaman Register jika diklik
        tvRegisterLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        tvforgotPasswordLink.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }
}