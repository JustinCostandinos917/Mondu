package com.example.mondu

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashboardSiswaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard_siswa)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        // Memaksa sistem menampilkan warna asli dari file ikon XML di drawable
        bottomNav.itemIconTintList = null

        // Pancingan: Menampilkan fragment Nilai Siswa secara otomatis saat pertama kali dibuka
        if (savedInstanceState == null) {
            bukaFragment(NilaiSiswaFragment())
        }

        // Listener perpindahan fragment saat menu diklik
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_siswa_nilai -> {
                    bukaFragment(NilaiSiswaFragment())
                    true
                }
                R.id.nav_siswa_kalender -> {
                    bukaFragment(KalenderFragment())
                    true
                }
                R.id.nav_siswa_profil -> {
                    bukaFragment(ProfilSiswaFragment())
                    true
                }
                else -> false
            }
        }
    }

    // Fungsi utilitas untuk mengganti isi FrameLayout secara dinamis
    private fun bukaFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
