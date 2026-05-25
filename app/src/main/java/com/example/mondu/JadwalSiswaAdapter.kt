package com.example.mondu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class JadwalSiswaAdapter(private val listJadwal: List<JadwalSiswa>) :
    RecyclerView.Adapter<JadwalSiswaAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvMapel: TextView = view.findViewById(R.id.tvMapelJadwal)
        val tvGuru: TextView = view.findViewById(R.id.tvGuruJadwal)
        val tvWaktu: TextView = view.findViewById(R.id.tvWaktuJadwal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_jadwal_siswa, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listJadwal[position]
        holder.tvMapel.text = data.nama_mapel
        holder.tvGuru.text = "Guru: ${data.nama_guru}"
        holder.tvWaktu.text = "Waktu: ${data.hari}, (${data.jam_mulai.substring(0, 5)} - ${data.jam_selesai.substring(0, 5)})"
    }

    override fun getItemCount(): Int = listJadwal.size
}
