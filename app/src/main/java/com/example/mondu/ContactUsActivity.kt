package com.example.mondu

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class ContactUsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)

        val btnBack = findViewById<ImageButton>(R.id.btnBackContact)
        val etSubject = findViewById<TextInputEditText>(R.id.etSubject)
        val etMessage = findViewById<TextInputEditText>(R.id.etMessage)
        val btnSend = findViewById<MaterialButton>(R.id.btnSendMessage)

        btnBack.setOnClickListener {
            finish()
        }

        btnSend.setOnClickListener {
            val subject = etSubject.text.toString().trim()
            val message = etMessage.text.toString().trim()

            if (subject.isEmpty() || message.isEmpty()) {
                Toast.makeText(this, "Harap isi subjek dan pesan", Toast.LENGTH_SHORT).show()
            } else {
                // Simulasi pengiriman pesan
                Toast.makeText(this, "Pesan berhasil dikirim!", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }
}
