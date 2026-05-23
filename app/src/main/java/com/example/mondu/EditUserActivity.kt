package com.example.mondu

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject
import java.util.Calendar

class EditUserActivity : AppCompatActivity() {
    private lateinit var etIdUser: TextInputEditText
    private lateinit var etUsername: TextInputEditText
    private lateinit var etNamaLengkap: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var actvRole: AutoCompleteTextView

    private lateinit var layoutInputSiswa: LinearLayout
    private lateinit var layoutInputGuru: LinearLayout
    private lateinit var etNis: TextInputEditText
    private lateinit var etNisn: TextInputEditText
    private lateinit var actvGender: AutoCompleteTextView
    private lateinit var etTanggalLahir: TextInputEditText
    private lateinit var etAlamat: TextInputEditText
    private lateinit var actvKelas: AutoCompleteTextView
    private lateinit var etNuptk: TextInputEditText
    private lateinit var btnUpdateUser: Button

    private val listKelasObjek = ArrayList<Kelas>()
    private var selectedIdKelas: String = ""
    private var currentRole: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_user)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        etIdUser = findViewById(R.id.etEditIdUser)
        etUsername = findViewById(R.id.etEditUsername)
        etNamaLengkap = findViewById(R.id.etEditNamaLengkap)
        etEmail = findViewById(R.id.etEditEmail)
        actvRole = findViewById(R.id.actvEditRole)
        layoutInputSiswa = findViewById(R.id.layoutEditInputSiswa)
        layoutInputGuru = findViewById(R.id.layoutEditInputGuru)
        etNis = findViewById(R.id.etEditNis)
        etNisn = findViewById(R.id.etEditNisn)
        actvGender = findViewById(R.id.actvEditGender)
        etTanggalLahir = findViewById(R.id.etEditTanggalLahir)
        etAlamat = findViewById(R.id.etEditAlamat)
        actvKelas = findViewById(R.id.actvEditKelas)
        etNuptk = findViewById(R.id.etEditNuptk)
        btnUpdateUser = findViewById(R.id.btnUpdateUser)

        // Set data dasar dari Intent lembaran fragment awal
        val idUser = intent.getStringExtra("id_user") ?: ""
        etIdUser.setText(idUser)
        etUsername.setText(intent.getStringExtra("username"))
        etNamaLengkap.setText(intent.getStringExtra("nama_lengkap"))
        etEmail.setText(intent.getStringExtra("email"))

        currentRole = intent.getStringExtra("role") ?: ""
        actvRole.setText(currentRole)

        // Tampilkan layout form tambahan bergantung role
        if (currentRole == "Siswa") {
            layoutInputSiswa.visibility = View.VISIBLE
            // Setup Gender
            actvGender.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, arrayOf("Laki-laki", "Perempuan")))
            // Ambil data Dropdown Kelas
            loadDropdownKelasDinamis()
        } else if (currentRole == "Guru") {
            layoutInputGuru.visibility = View.VISIBLE
        }

        // Ambil sisa data detail spesifik (NISN, Alamat, NIP, dll) dari Database
        loadDetailUserDariDatabase(idUser)

        // Setup DatePicker
        etTanggalLahir.setOnClickListener {
            val c = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, day ->
                etTanggalLahir.setText(String.format("%04d-%02d-%02d", year, month + 1, day))
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
        }

        actvKelas.setOnItemClickListener { parent, _, position, _ ->
            val kelasObj = parent.getItemAtPosition(position) as Kelas
            selectedIdKelas = kelasObj.id_kelas
        }

        btnUpdateUser.setOnClickListener { updateDataKeDatabase() }
    }

    private fun loadDropdownKelasDinamis() {
        val url = "http://10.0.2.2/api_mondu/get_kelas.php"
        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    listKelasObjek.clear()
                    var defaultPosition = -1
                    for (i in 0 until response.length()) {
                        val obj = response.getJSONObject(i)
                        val kelas = Kelas(obj.getString("id_kelas"), obj.getString("nama_kelas"))
                        listKelasObjek.add(kelas)

                        // Menandai posisi dropdown biar auto-select sesuai id_kelas bawaan si siswa
                        if (kelas.id_kelas == selectedIdKelas) {
                            defaultPosition = i
                        }
                    }
                    val adapter = ArrayAdapter(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        listKelasObjek
                    )
                    actvKelas.setAdapter(adapter)

                    if (defaultPosition != -1) {
                        actvKelas.setText(listKelasObjek[defaultPosition].nama_kelas, false)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, { error -> error.printStackTrace() })
        Volley.newRequestQueue(this).add(request)
    }

    private fun loadDetailUserDariDatabase(idUser: String) {
        val url = "http://10.0.2.2/api_mondu/get_edit_user.php?id_user=$idUser&role=$currentRole"
        val stringRequest = StringRequest(Request.Method.GET, url,
            { response ->
                try {
                    val res = JSONObject(response)
                    if (res.getString("status") == "success") {
                        val data = res.getJSONObject("data")
                        if (currentRole == "Siswa") {
                            etNis.setText(data.getString("nis"))
                            etNisn.setText(data.getString("nisn"))
                            etAlamat.setText(data.getString("alamat"))
                            etTanggalLahir.setText(data.getString("tanggal_lahir"))

                            val jk = data.getString("jenis_kelamin")
                            actvGender.setText(if (jk == "L") "Laki-laki" else "Perempuan", false)

                            selectedIdKelas = data.getString("id_kelas")
                            // Re-load dropdown untuk sinkronisasi posisi teks terpilih
                            loadDropdownKelasDinamis()
                        } else if (currentRole == "Guru") {
                            etNuptk.setText(data.getString("nip"))
                        }
                    }
                } catch (e: Exception) { e.printStackTrace() }
            }, { error -> error.printStackTrace() })
        Volley.newRequestQueue(this).add(stringRequest)
    }

    private fun updateDataKeDatabase() {
        val url = "http://10.0.2.2/api_mondu/update_user.php"
        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            { response ->
                try {
                    val res = JSONObject(response)
                    Toast.makeText(this, res.getString("message"), Toast.LENGTH_SHORT).show()
                    if (res.getString("status") == "success") finish() // Tutup activity, kembali ke list
                } catch (e: Exception) { e.printStackTrace() }
            }, { error -> Toast.makeText(this, "Error koneksi internet", Toast.LENGTH_SHORT).show() }) {

            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["id_user"] = etIdUser.text.toString().trim()
                params["username"] = etUsername.text.toString().trim()
                params["nama_lengkap"] = etNamaLengkap.text.toString().trim()
                params["email"] = etEmail.text.toString().trim()
                params["role"] = currentRole

                if (currentRole == "Siswa") {
                    params["nis"] = etNis.text.toString().trim()
                    params["nisn"] = etNisn.text.toString().trim()
                    params["alamat"] = etAlamat.text.toString().trim()
                    params["tanggal_lahir"] = etTanggalLahir.text.toString().trim()
                    params["jenis_kelamin"] = if (actvGender.text.toString() == "Laki-laki") "L" else "P"
                    params["id_kelas"] = selectedIdKelas
                } else if (currentRole == "Guru") {
                    params["nuptk"] = etNuptk.text.toString().trim()
                }
                return params
            }
        }
        Volley.newRequestQueue(this).add(stringRequest)
    }
}