package com.example.mondu

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class JadwalActivity : AppCompatActivity() {
    companion object {
        var idKelasDipilih: String = "" // Variabel statis untuk akses ke Fragment
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_jadwal)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        idKelasDipilih = intent.getStringExtra("id_kelas") ?: ""

        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val fabTambah = findViewById<FloatingActionButton>(R.id.fabTambah)

        // Setup Tab
        val hariList = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat")
        val fragments = hariList.map { hari -> JadwalFragment(hari) }
        fabTambah.setOnClickListener {
            // 1. Ambil posisi tab yang sedang aktif
            val currentPosition = viewPager.currentItem
            val hariAktif = hariList[currentPosition]

            // 2. Kirim data ke AddJadwalActivity
            val intent = Intent(this, AddJadwalActivity::class.java)
            intent.putExtra("ID_KELAS", idKelasDipilih)
            intent.putExtra("HARI", hariAktif)
            startActivity(intent)
        }
        viewPager.adapter = ViewPagerAdapter(this, fragments)

        TabLayoutMediator(tabLayout, viewPager) { tab, pos ->
            tab.text = hariList[pos]
        }.attach()
    }
}