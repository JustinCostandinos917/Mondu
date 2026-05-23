//package com.example.mondu
//
//import android.app.Dialog
//import android.app.TimePickerDialog
//import android.os.Bundle
//import android.view.View
//import android.widget.AdapterView
//import android.widget.ArrayAdapter
//import android.widget.AutoCompleteTextView
//import android.widget.Button
//import android.widget.Spinner
//import android.widget.TextView
//import android.widget.Toast
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.android.volley.Request
//import com.android.volley.toolbox.JsonArrayRequest
//import com.android.volley.toolbox.StringRequest
//import com.android.volley.toolbox.Volley
//import com.google.android.material.textfield.TextInputEditText
//import org.json.JSONObject
//
//class AddJadwalActivity : AppCompatActivity() {
//    private lateinit var rvJadwal: RecyclerView
//    private val listJadwal = ArrayList<JamSlot>()
//    private lateinit var adapter: JadwalAdapter
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_add_jadwal)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//        rvJadwal = findViewById(R.id.rvJadwal)
//        rvJadwal.layoutManager = LinearLayoutManager(this)
//
//        // 1. Generate Slot Waktu statis
//        generateSlots()
//
//        // 2. Setup Adapter
//        adapter = JadwalAdapter(listJadwal) { slot ->
//            showDialogPilihJadwal(slot)
//        }
//        rvJadwal.adapter = adapter
//    }
//
//    private fun generateSlots() {
//        listJadwal.add(JamSlot("07:00:00", "08:30:00"))
//        listJadwal.add(JamSlot("08:30:00", "10:00:00"))
//        // Tambah slot lainnya...
//    }
//
//    private fun showDialogPilihJadwal(slot: JamSlot) {
//        val dialog = Dialog(this)
//        dialog.setContentView(R.layout.dialog_input_jadwal)
//
//        val spinnerMapel = dialog.findViewById<Spinner>(R.id.spinnerMapel)
//        val spinnerGuru = dialog.findViewById<Spinner>(R.id.spinnerGuru)
//        val btnSimpan = dialog.findViewById<Button>(R.id.btnSimpan)
//
//        // Logika saat Mapel dipilih -> Guru otomatis terkunci
//        spinnerMapel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
//                // Asumsi listMapel sudah di-load
//                val nuptkGuru = listMapel[pos].nuptk
//                val index = listGuru.indexOfFirst { it.nuptk == nuptkGuru }
//                spinnerGuru.setSelection(index)
//                spinnerGuru.isEnabled = false // LOCKED!
//            }
//            override fun onNothingSelected(p0: AdapterView<*>?) {}
//        }
//
//        btnSimpan.setOnClickListener {
//            // KIRIM DATA via Volley ke save_jadwal.php
//            // Kirim: id_kelas, id_mapel, nuptk, hari, slot.jamMulai, slot.jamSelesai
//        }
//        dialog.show()
//    }
//}