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

class AddUserActivity : AppCompatActivity() {
    private lateinit var etUsername: TextInputEditText
    private lateinit var etNamaLengkap: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var actvRole: AutoCompleteTextView

    private lateinit var layoutInputSiswa: LinearLayout
    private lateinit var layoutInputGuru: LinearLayout
    private lateinit var actvKelas: AutoCompleteTextView
    private lateinit var etTanggalLahir: TextInputEditText
    private lateinit var actvGender: AutoCompleteTextView
    private lateinit var btnSimpanUser: Button

    private val listKelasObjek = ArrayList<Kelas>()
    private var selectedIdKelas: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_user)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Inisialisasi View
        etUsername = findViewById(R.id.etUsername)
        etNamaLengkap = findViewById(R.id.etNamaLengkap)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        actvRole = findViewById(R.id.actvRole)
        layoutInputSiswa = findViewById(R.id.layoutInputSiswa)
        layoutInputGuru = findViewById(R.id.layoutInputGuru)
        actvKelas = findViewById(R.id.actvKelas)
        etTanggalLahir = findViewById(R.id.etTanggalLahir)
        actvGender = findViewById(R.id.actvGender)
        btnSimpanUser = findViewById(R.id.btnSimpanUser)

//        findViewById<View>(R.id.btnBackAddUser).setOnClickListener {
//            finish()
//        }

        // 1. Setup Dropdown Role
        val listRole = arrayOf("Admin", "Guru", "Wali Kelas", "Siswa")
        actvRole.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, listRole))

        actvRole.setOnItemClickListener { parent, _, position, _ ->
            val role = parent.getItemAtPosition(position).toString()
            layoutInputSiswa.visibility = if (role == "Siswa") View.VISIBLE else View.GONE
            layoutInputGuru.visibility = if (role == "Guru") View.VISIBLE else View.GONE
        }

        // 2. Setup Dropdown Gender (L/P)
        val listGender = arrayOf("Laki-laki", "Perempuan")
        actvGender.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, listGender))

        // 3. Ambil data kelas DINAMIS dari MySQL via Volley
        loadDropdownKelasDinamis()
        actvKelas.setOnItemClickListener { parent, _, position, _ ->
            val kelasObj = parent.getItemAtPosition(position) as Kelas
            selectedIdKelas = kelasObj.id_kelas // Menyimpan ID "KLS001", dst.
        }

        // 4. Date Picker Dialog untuk tanggal lahir
        etTanggalLahir.setOnClickListener {
            val c = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, day ->
                val formattedDate = String.format("%04d-%02d-%02d", year, month + 1, day)
                etTanggalLahir.setText(formattedDate)
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
        }

        btnSimpanUser.setOnClickListener { simpanUserKeDatabase() }
    }

    private fun loadDropdownKelasDinamis() {
        val url = "http://10.0.2.2/api_mondu/get_kelas.php"
        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    listKelasObjek.clear()
                    for (i in 0 until response.length()) {
                        val obj = response.getJSONObject(i)
                        listKelasObjek.add(
                            Kelas(
                                obj.getString("id_kelas"),
                                obj.getString("nama_kelas")
                            )
                        )
                    }
                    actvKelas.setAdapter(
                        ArrayAdapter(
                            this,
                            android.R.layout.simple_dropdown_item_1line,
                            listKelasObjek
                        )
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, { error -> error.printStackTrace() })
        Volley.newRequestQueue(this).add(request)
    }

    private fun simpanUserKeDatabase() {
        val username = etUsername.text.toString().trim()
        val nama = etNamaLengkap.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val role = actvRole.text.toString().trim()

        if (username.isEmpty() || nama.isEmpty() || email.isEmpty() || password.isEmpty() || role.isEmpty()) {
            Toast.makeText(this, "Semua form utama wajib diisi!", Toast.LENGTH_SHORT).show()
            return
        }

        val url = "http://10.0.2.2/api_mondu/tambah_user.php"
        val stringRequest = object : StringRequest(Request.Method.POST, url,
            { response ->
                try {
                    val res = JSONObject(response)
                    Toast.makeText(this, res.getString("message"), Toast.LENGTH_SHORT).show()
                    if (res.getString("status") == "success") finish()
                } catch (e: Exception) { e.printStackTrace() }
            }, { error -> Toast.makeText(this, "Koneksi Error: ${error.message}", Toast.LENGTH_SHORT).show() }) {

            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["username"] = username
                params["nama_lengkap"] = nama
                params["email"] = email
                params["password"] = password
                params["role"] = role

                if (role == "Siswa") {
                    params["nis"] = findViewById<TextInputEditText>(R.id.etNis).text.toString().trim()
                    params["nisn"] = findViewById<TextInputEditText>(R.id.etNisn).text.toString().trim()
                    params["alamat"] = findViewById<TextInputEditText>(R.id.etAlamat).text.toString().trim()
                    params["tanggal_lahir"] = etTanggalLahir.text.toString().trim()
                    params["jenis_kelamin"] = if (actvGender.text.toString() == "Laki-laki") "L" else "P"
                    params["id_kelas"] = selectedIdKelas // Mengirimkan ID dinamis dari database
                } else if (role == "Guru") {
                    params["nuptk"] = findViewById<TextInputEditText>(R.id.etNip).text.toString().trim()
                }
                return params
            }
        }
        Volley.newRequestQueue(this).add(stringRequest)
    }
}