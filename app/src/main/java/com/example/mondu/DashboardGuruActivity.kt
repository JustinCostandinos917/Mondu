package com.example.mondu

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashboardGuruActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard_guru)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.itemIconTintList = null

        // 1. CEK STATUS WALI KELAS
        val prefs = getSharedPreferences("MonduSession", MODE_PRIVATE)
        val isWaliKelas = prefs.getBoolean("is_walikelas", false)

        bottomNav.menu.clear() // Bersihkan menu terlebih dahulu
        if (isWaliKelas) {
            bottomNav.inflateMenu(R.menu.menu_navigation_walikelas)
        } else {
            bottomNav.inflateMenu(R.menu.menu_navigation_guru)
        }

        // 3. DEFAULT FRAGMENT
        if (savedInstanceState == null) {
            bukaFragment(HomeGuruFragment())
        }

        // 4. LISTENER NAVIGASI
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    bukaFragment(HomeGuruFragment())
                    true
                }
                R.id.nav_jadwal -> {
                    bukaFragment(JadwalGuruFragment())
                    true
                }
                R.id.nav_input_nilai -> {
                    bukaFragment(InputNilaiFragment())
                    true
                }
                R.id.nav_wali_kelas -> {
                    // Hanya buka fragment jika dia memang Wali Kelas
                    if (isWaliKelas) {
                        bukaFragment(WaliKelasFragment())
                        true
                    } else {
                        false
                    }
                }
                else -> false
            }
        }
    }

    // Fungsi helper yang sama agar kodingan rapi
    private fun bukaFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}