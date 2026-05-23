package com.example.mondu

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class AturWaliActivity : AppCompatActivity() {
    private lateinit var actvPilihWali: AutoCompleteTextView
    private lateinit var btnSimpanWali: Button

    private var selectedIdKelas: String = ""
    private var selectedNuptkGuru: String = "" // Menampung NUPTK guru yang dipilih
    private val listGuruObjek = ArrayList<GuruDropdown>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_atur_wali)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        actvPilihWali = findViewById(R.id.actvPilihWali)
        btnSimpanWali = findViewById(R.id.btnSimpanWali)

        // Tangkap data lemparan intent dari AkademikFragment
        selectedIdKelas = intent.getStringExtra("id_kelas") ?: ""
        val namaKelas = intent.getStringExtra("nama_kelas") ?: ""
        findViewById<TextView>(R.id.tvFormWaliTitle).text = "Atur Wali Kelas - $namaKelas"

        // 1. Load list guru untuk diisikan ke dropdown menu
        loadDropdownGuru()

        // 2. Deteksi item guru yang diklik dari dropdown
        actvPilihWali.setOnItemClickListener { parent, _, position, _ ->
            val guruObj = parent.getItemAtPosition(position) as GuruDropdown
            selectedNuptkGuru = guruObj.nuptk // Ambil NUPTK-nya
        }

        // 3. Tombol simpan untuk mengirim data perubahan ke server
        btnSimpanWali.setOnClickListener {
            if (selectedNuptkGuru.isEmpty()) {
                Toast.makeText(this, "Silakan pilih guru terlebih dahulu!", Toast.LENGTH_SHORT).show()
            } else {
                simpanWaliKelasKeDatabase()
            }
        }
    }

    private fun loadDropdownGuru() {
        val url = "http://10.0.2.2/api_mondu/get_guru_tersedia.php"

        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    listGuruObjek.clear()
                    for (i in 0 until response.length()) {
                        val obj = response.getJSONObject(i)
                        // Pastikan model GuruDropdown kamu sudah memakai properti nuptk ya, Jus!
                        listGuruObjek.add(
                            GuruDropdown(
                                obj.getString("nuptk"),
                                obj.getString("nama_lengkap")
                            )
                        )
                    }
                    val adapter = ArrayAdapter(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        listGuruObjek
                    )
                    actvPilihWali.setAdapter(adapter)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            { error ->
                Toast.makeText(this, "Gagal memuat list guru", Toast.LENGTH_SHORT).show()
            })

        Volley.newRequestQueue(this).add(request)
    }

    private fun simpanWaliKelasKeDatabase() {
        val url = "http://10.0.2.2/api_mondu/set_walikelas.php"

        val stringRequest = object : StringRequest(Request.Method.POST, url,
            { response ->
                try {
                    val res = JSONObject(response)
                    Toast.makeText(this, res.getString("message"), Toast.LENGTH_SHORT).show()
                    if (res.getString("status") == "success") {
                        finish() // Tutup halaman form dan kembali ke list kelas
                    }
                } catch (e: Exception) { e.printStackTrace() }
            },
            { error ->
                Toast.makeText(this, "Terjadi gangguan jaringan server", Toast.LENGTH_SHORT).show()
            }) {

            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["id_kelas"] = selectedIdKelas
                params["nuptk"] = selectedNuptkGuru // Parameter dioper ke query PHP
                return params
            }
        }

        Volley.newRequestQueue(this).add(stringRequest)
    }
}