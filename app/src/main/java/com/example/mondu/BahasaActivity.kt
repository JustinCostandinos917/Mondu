package com.example.mondu

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import java.util.Locale

class BahasaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bahasa)

        val btnBack = findViewById<ImageButton>(R.id.btn_back_bahasa)
        val btnIndo = findViewById<MaterialCardView>(R.id.btn_lang_indo)
        val btnEng = findViewById<MaterialCardView>(R.id.btn_lang_eng)
        val rbIndo = findViewById<RadioButton>(R.id.rb_indo)
        val rbEng = findViewById<RadioButton>(R.id.rb_eng)

        // Default selection (simulasi, aslinya ambil dari SharedPreferences)
        rbIndo.isChecked = true

        btnBack.setOnClickListener {
            finish()
        }

        btnIndo.setOnClickListener {
            rbIndo.isChecked = true
            rbEng.isChecked = false
            updateLanguage("in")
        }

        btnEng.setOnClickListener {
            rbIndo.isChecked = false
            rbEng.isChecked = true
            updateLanguage("en")
        }
    }

    private fun updateLanguage(langCode: String) {
        val message = if (langCode == "in") "Bahasa Indonesia dipilih" else "English selected"
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        // Logika perpindahan bahasa (Locale) bisa ditambahkan di sini
    }
}