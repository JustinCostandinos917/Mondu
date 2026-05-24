package com.example.mondu

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
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
import com.google.android.material.button.MaterialButton

class AddJadwalActivity : AppCompatActivity() {
    private val mapelList = mutableListOf<Map<String, String>>()
    private val jamList = mutableListOf<Map<String, String>>()
    // Variabel class agar bisa diakses di semua fungsi
    private var idKelas: String? = null
    private var hari: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_jadwal)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        idKelas = intent.getStringExtra("ID_KELAS")
        hari = intent.getStringExtra("HARI") ?: ""

        setupDropdownMapel()

        findViewById<MaterialButton>(R.id.btnSimpan).setOnClickListener {
            tambahDataKeServer()
        }
    }

    private fun setupDropdownMapel() {
        val url = "http://10.0.2.2/api_mondu/get_mapel.php"
        val request = JsonArrayRequest(Request.Method.GET, url, null, { response ->
            mapelList.clear()
            for (i in 0 until response.length()) {
                val obj = response.getJSONObject(i)
                mapelList.add(mapOf(
                    "id" to obj.getString("id_mapel"),
                    "nama" to obj.getString("nama_mapel"),
                    "nama_guru" to obj.getString("nama_guru"),
                    "nuptk" to obj.getString("nuptk")
                ))
            }
            val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, mapelList.map { it["nama"] })
            val dropdown = findViewById<AutoCompleteTextView>(R.id.dropdownMapel)
            val tvGuru = findViewById<TextView>(R.id.tvInfoGuru)

            dropdown.setAdapter(adapter)

            dropdown.setOnItemClickListener { parent, _, position, _ ->
                val selectedNama = parent.getItemAtPosition(position).toString()
                val mapelTerpilih = mapelList.find { it["nama"] == selectedNama }
                tvGuru.text = "Guru: ${mapelTerpilih?.get("nama_guru") ?: "-"}"

                val nuptk = mapelTerpilih?.get("nuptk") ?: ""

                findViewById<AutoCompleteTextView>(R.id.dropdownJam).setText("", false)

                setupDropdownJam(idKelas, hari, nuptk)
            }
        }, { it.printStackTrace() })
        Volley.newRequestQueue(this).add(request)
    }

    private fun setupDropdownJam(idKelas: String?, hari: String, nuptk: String) {
        val url = "http://10.0.2.2/api_mondu/get_jam.php?id_kelas=$idKelas&hari=$hari&nuptk=$nuptk"
        val request = JsonArrayRequest(Request.Method.GET, url, null, { response ->
            jamList.clear()
            for (i in 0 until response.length()) {
                val obj = response.getJSONObject(i)
                jamList.add(mapOf(
                    "id" to obj.getString("id_jam"),
                    "nama" to "${obj.getString("jam_mulai")} - ${obj.getString("jam_selesai")}"
                ))
            }
            val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, jamList.map { it["nama"] })
            findViewById<AutoCompleteTextView>(R.id.dropdownJam).setAdapter(adapter)
        }, { it.printStackTrace() })
        Volley.newRequestQueue(this).add(request)
    }

    private fun tambahDataKeServer() {
        // Ambil data yang dipilih user
        val selectedMapelNama = findViewById<AutoCompleteTextView>(R.id.dropdownMapel).text.toString()
        val selectedMapelId = mapelList.find { it["nama"] == selectedMapelNama }?.get("id")

        val mapelTerpilih = mapelList.find { it["nama"] == selectedMapelNama }
        val nuptkGuru = mapelTerpilih?.get("nuptk") ?: ""

        val selectedJamNama = findViewById<AutoCompleteTextView>(R.id.dropdownJam).text.toString()
        val selectedJamId = jamList.find { it["nama"] == selectedJamNama }?.get("id")

        if (selectedMapelId == null || selectedJamId == null) {
            Toast.makeText(this, "Pilih mapel dan jam yang valid!", Toast.LENGTH_SHORT).show()
            return
        }

        val url = "http://10.0.2.2/api_mondu/tambah_jadwal.php"
        val stringRequest = object : StringRequest(Method.POST, url, {
            Toast.makeText(this, "Berhasil menambah jadwal", Toast.LENGTH_SHORT).show()
            finish()
        }, { Toast.makeText(this, "Gagal: ${it.message}", Toast.LENGTH_SHORT).show() }) {
            override fun getParams(): Map<String, String> {
                return mapOf(
                    "id_kelas" to (idKelas ?: ""),
                    "id_mapel" to selectedMapelId,
                    "id_jam" to selectedJamId,
                    "hari" to hari,
                    "nuptk" to nuptkGuru
                )
            }
        }
        Volley.newRequestQueue(this).add(stringRequest)
    }
}