package com.example.mondu

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val hariList: List<String>
) : FragmentStateAdapter(fragmentActivity) {

    // Menentukan berapa banyak halaman (tab) yang ada
    override fun getItemCount(): Int = hariList.size

    // Membuat fragment untuk setiap posisi tab
    override fun createFragment(position: Int): Fragment {
        // Mengirim nama hari ke JadwalFragment yang kita buat sebelumnya
        return JadwalFragment(hariList[position])
    }
}