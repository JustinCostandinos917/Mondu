package com.example.mondu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashboardSiswaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_siswa)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.itemIconTintList = null

        // Default awal: Buka HomeSiswaFragment
        if (savedInstanceState == null) {
            bukaFragment(HomeSiswaFragment())
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    bukaFragment(HomeSiswaFragment())
                    true
                }
                R.id.nav_jadwal -> {
                    bukaFragment(JadwalSiswaFragment())
                    true
                }
                R.id.nav_absensi -> {
                    bukaFragment(AbsensiSiswaFragment())
                    true
                }
                R.id.nav_setting -> {
                    bukaFragment(SettingSiswaFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun bukaFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}