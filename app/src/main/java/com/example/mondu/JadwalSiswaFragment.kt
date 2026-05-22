package com.example.mondu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout

class JadwalSiswaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_jadwal_siswa, container, false)

        val layoutJadwal = view.findViewById<LinearLayout>(R.id.layoutJadwalHarian)
        val layoutKalender = view.findViewById<LinearLayout>(R.id.layoutFullKalender)
        val btnBukaKalender = view.findViewById<Button>(R.id.btnBukaKalender)
        val btnKembali = view.findViewById<Button>(R.id.btnKembaliKeJadwal)

        btnBukaKalender.setOnClickListener {
            layoutJadwal.visibility = View.GONE
            layoutKalender.visibility = View.VISIBLE
        }

        btnKembali.setOnClickListener {
            layoutKalender.visibility = View.GONE
            layoutJadwal.visibility = View.VISIBLE
        }

        return view
    }
}