package com.example.mondu

import android.content.Intent
import android.os.Bundle
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

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Binding ID sesuai dengan XML register barumu
        val etName = findViewById<TextInputEditText>(R.id.etRegisterName)
        val etUsername = findViewById<TextInputEditText>(R.id.etRegisterUsername)
        val etPassword = findViewById<TextInputEditText>(R.id.etRegisterPassword)
        val etConfirmPassword = findViewById<TextInputEditText>(R.id.etRegisterConfirmPassword)
        val btnRegisterSubmit = findViewById<Button>(R.id.btnRegisterSubmit)
        val tvLoginLink = findViewById<TextView>(R.id.tvLoginLink)

        val urlRegister = "http://10.0.2.2/api_mondu/register.php"

        btnRegisterSubmit.setOnClickListener {
            val namaLengkap = etName.text.toString().trim()
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            // Validasi kelengkapan data
            if (namaLengkap.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Semua data wajib diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validasi apakah password dan konfirmasinya sama cocok
            if (password != confirmPassword) {
                Toast.makeText(this, "Kata Sandi tidak cocok, periksa kembali!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Eksekusi pengiriman akun baru ke database MySQL via Volley
            val stringRequest = object : StringRequest(Request.Method.POST, urlRegister,
                Response.Listener { response ->
                    try {
                        val jsonObject = JSONObject(response)
                        val status = jsonObject.getString("status")
                        val message = jsonObject.getString("message")

                        if (status == "success") {
                            Toast.makeText(this, "Akun berhasil dibuat! Silakan masuk.", Toast.LENGTH_LONG).show()
                            // Balikkan otomatis ke login jika berhasil daftar
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this, "Gagal memproses data server: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener { error ->
                    Toast.makeText(this, "Gagal terhubung ke MySQL: ${error.message}", Toast.LENGTH_LONG).show()
                }
            ) {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["nama_lengkap"] = namaLengkap
                    params["username"] = username
                    params["password"] = password
                    params["role"] = "Siswa" // Default register mandiri otomatis jadi akun Siswa
                    return params
                }
            }

            Volley.newRequestQueue(this).add(stringRequest)
        }

        // Jika tidak jadi daftar, klik untuk balik ke Login
        tvLoginLink.setOnClickListener {
            finish()
        }
    }
}