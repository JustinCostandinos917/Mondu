package com.example.mondu

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AbsensiSiswaAdapter(private val list: List<AbsensiSiswa>) :
    RecyclerView.Adapter<AbsensiSiswaAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvMapel: TextView = view.findViewById(R.id.tvMapelAbsensi)
        val tvStatus: TextView = view.findViewById(R.id.tvStatusAbsensi)
        val tvWaktu: TextView = view.findViewById(R.id.tvWaktuAbsensi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_absensi, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.tvMapel.text = item.nama_mapel
        holder.tvStatus.text = item.status
        holder.tvWaktu.text = "${item.tanggal} (${item.jam_mulai} - ${item.jam_selesai})"

        // Atur warna status
        when (item.status.lowercase()) {
            "hadir" -> {
                holder.tvStatus.setTextColor(Color.parseColor("#03543F"))
                holder.tvStatus.setBackgroundColor(Color.parseColor("#DEF7EC"))
            }
            "sakit" -> {
                holder.tvStatus.setTextColor(Color.parseColor("#92400E"))
                holder.tvStatus.setBackgroundColor(Color.parseColor("#FEF3C7"))
            }
            "izin" -> {
                holder.tvStatus.setTextColor(Color.parseColor("#1E40AF"))
                holder.tvStatus.setBackgroundColor(Color.parseColor("#DBEAFE"))
            }
            "alpha" -> {
                holder.tvStatus.setTextColor(Color.parseColor("#991B1B"))
                holder.tvStatus.setBackgroundColor(Color.parseColor("#FEE2E2"))
            }
        }
    }

    override fun getItemCount(): Int = list.size
}
