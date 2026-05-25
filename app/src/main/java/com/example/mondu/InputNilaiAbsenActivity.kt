package com.example.mondu

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class InputNilaiAbsenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_input_nilai_absen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val idJadwal = intent.getStringExtra("id_jadwal") ?: "" // Pastikan di JadwalGuruFragment kamu juga kirim id_jadwal
        val idKelas = intent.getStringExtra("id_kelas") ?: ""

        // 2. Kirim data tersebut ke AbsensiFragment menggunakan Bundle
        val bundle = Bundle()
        bundle.putString("id_jadwal", idJadwal)
        bundle.putString("id_kelas", idKelas)

        val viewPager = findViewById<ViewPager2>(R.id.viewPagerInput)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayoutInput)

        val absensiFrag = AbsensiFragment().apply { arguments = bundle }
        val nilaiFrag = NilaiPelajaranFragment().apply { arguments = bundle }

        val fragments = mutableListOf<Fragment>(absensiFrag, nilaiFrag)

        // LOGIKA GURU ESKUL
        val sharedPref = getSharedPreferences("MonduSession", Context.MODE_PRIVATE)
        val isGuruEskul = sharedPref.getBoolean("is_eskul", false) // Fungsi buatanmu (ambil dari Session)
        if (isGuruEskul) {
            fragments.add(NilaiEskulFragment())
        }

        viewPager.adapter = ViewPagerAdapter(this, fragments)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Absensi"
                1 -> "Nilai Pelajaran"
                2 -> "Nilai Eskul"
                else -> ""
            }
        }.attach()
    }
}