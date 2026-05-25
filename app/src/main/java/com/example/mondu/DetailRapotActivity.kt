package com.example.mondu

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class DetailRapotActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_rapot)

        val btnBack = findViewById<ImageButton>(R.id.btnBackRapot)
        btnBack.setOnClickListener {
            finish()
        }

        val tvNama = findViewById<android.widget.TextView>(R.id.tvNamaSiswaDetail)
        val tvKelas = findViewById<android.widget.TextView>(R.id.tvKelasDetail)
        val tvTahun = findViewById<android.widget.TextView>(R.id.tvTahunDetail)

        tvNama.text = intent.getStringExtra("NAMA_SISWA") ?: "Ahmad Fauzi"
        tvKelas.text = "Kelas ${intent.getStringExtra("KELAS") ?: "7A"}"
        tvTahun.text = intent.getStringExtra("TAHUN") ?: "2025/2026"
    }
}