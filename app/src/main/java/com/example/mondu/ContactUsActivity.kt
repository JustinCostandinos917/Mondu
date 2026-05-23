package com.example.mondu

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class ContactUsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_contact_us)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val etPesanKontak = findViewById<TextInputEditText>(R.id.et_pesan_kontak)
        val btnKirim = findViewById<MaterialButton>(R.id.btn_kirim_pesan)

        btnKirim.setOnClickListener {
            val pesan = etPesanKontak.text.toString().trim()

            if (pesan.isEmpty()) {
                Toast.makeText(this, "Pesan tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            } else {
                // Proses kirim ke database atau email tim developer
                Toast.makeText(this, "Laporan berhasil dikirim, terima kasih!", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }
}