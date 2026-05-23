package com.example.mondu

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject

class GantiPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ganti_password)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val etPasswordLama = findViewById<TextInputEditText>(R.id.et_password_lama)
        val etPasswordBaru = findViewById<TextInputEditText>(R.id.et_password_baru)
        val btnSimpan = findViewById<MaterialButton>(R.id.btn_simpan_password)

        btnSimpan.setOnClickListener {
            val passLama = etPasswordLama.text.toString().trim()
            val passBaru = etPasswordBaru.text.toString().trim()

            // Anggap saja ini username user yang sedang login saat ini.
            // Nanti bisa kamu ganti dinamis pake data dari SharedPreferences/Session login kamu.
            val usernameAktif = "siswa123"

            if (passLama.isEmpty() || passBaru.isEmpty()) {
                Toast.makeText(this, "Semua kolom wajib diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // GANTI IP INI DENGAN IP LAPTOP KAMU (Bisa dicek lewat CMD ketik 'ipconfig')
            // Jangan pakai 'localhost' karena emulator membaca localhost sebagai dirinya sendiri.
            val urlAPI = "http://10.0.2.2/api_mondu/update_password.php"

            // Membuat request POST via Volley
            val queue = Volley.newRequestQueue(this)
            val stringRequest = object : StringRequest(
                Method.POST, urlAPI,
                Response.Listener { response ->
                    try {
                        // Membaca response JSON dari PHP
                        val jsonObject = JSONObject(response)
                        val status = jsonObject.getString("status")
                        val message = jsonObject.getString("message")

                        if (status == "success") {
                            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                            finish() // Tutup halaman jika berhasil
                        } else {
                            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(this, "Error parsing data!", Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener { error ->
                    Toast.makeText(this, "Koneksi ke server gagal: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            ) {
                // Mengirimkan parameter POST ke file PHP
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["username"] = usernameAktif
                    params["password_lama"] = passLama
                    params["password_baru"] = passBaru
                    return params
                }
            }

            // Jalankan request ke server
            queue.add(stringRequest)
        }
    }
}