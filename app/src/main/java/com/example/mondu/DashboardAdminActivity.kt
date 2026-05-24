package com.example.mondu

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashboardAdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard_admin)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        if (savedInstanceState == null) {
            bukaFragment(DashboardAdminFragment())
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> {
                    bukaFragment(DashboardAdminFragment())
                    true
                }
                R.id.nav_users -> {
                    bukaFragment(KelolaUserFragment())
                    true
                }
                R.id.nav_logs -> {
                    bukaFragment(LogAktivitasFragment())
                    true
                }
                R.id.nav_akademik -> {
                    bukaFragment(AkademikFragment())
                    true
                }
                R.id.nav_arsip -> {
                    bukaFragment(ArsipFragment())
                    true
                }
                else -> false
            }
        }
    }

    fun setBottomNavigationItem(itemId: Int) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.selectedItemId = itemId
    }

    private fun bukaFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}