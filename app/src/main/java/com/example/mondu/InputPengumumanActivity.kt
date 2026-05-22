package com.example.mondu

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class InputPengumumanActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_pengumuman)

        val etJudul = findViewById<EditText>(R.id.etInputJudul)
        val etKonten = findViewById<EditText>(R.id.etInputKonten)
        val btnKirim = findViewById<Button>(R.id.btnKirimPengumuman)

        // Alamat URL API PHP kamu (Sesuaikan IP jika pakai HP asli atau Emulator)
        // 10.0.2.2 adalah localhost-nya emulator bawaan Android Studio
        val urlAPI = "http://10.0.2.2/api_mondu/insert_pengumuman.php"

        // Simulasi ID User yang sedang login (Misal: Pak Budi [ID: 1])
        val idUserLogin = "1"

        btnKirim.setOnClickListener {
            val judul = etJudul.text.toString().trim()
            val konten = etKonten.text.toString().trim()

            if (judul.isEmpty() || konten.isEmpty()) {
                Toast.makeText(this, "Semua field wajib diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 1. Membuat Request Volley (Metode POST)
            val stringRequest = object : StringRequest(
                Request.Method.POST, urlAPI,
                Response.Listener { response ->
                    try {
                        // Membaca response JSON sukses/gagal dari PHP
                        val jsonObject = JSONObject(response)
                        val status = jsonObject.getString("status")
                        val message = jsonObject.getString("message")

                        if (status == "success") {
                            Toast.makeText(this, "Sukses! Data masuk ke MySQL", Toast.LENGTH_SHORT).show()
                            finish() // Kembali ke halaman sebelumnya otomatis
                        } else {
                            Toast.makeText(this, "Gagal: $message", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this, "Format Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener { error ->
                    Toast.makeText(this, "Error Jaringan: ${error.message}", Toast.LENGTH_LONG).show()
                }
            ) {
                // 2. Mengirimkan parameter data (id_user, judul, konten) ke PHP
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["id_user"] = idUserLogin
                    params["judul"] = judul
                    params["konten"] = konten
                    return params
                }
            }

            // 3. Masukkan request ke dalam antrean Volley untuk dieksekusi
            val requestQueue = Volley.newRequestQueue(this)
            requestQueue.add(stringRequest)
        }
    }
}