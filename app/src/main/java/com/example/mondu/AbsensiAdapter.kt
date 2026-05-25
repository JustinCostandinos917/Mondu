package com.example.mondu

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AbsensiAdapter(private val listAbsensi: List<AbsensiSiswa>) :
    RecyclerView.Adapter<AbsensiAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvMapel: TextView = view.findViewById(R.id.tvMapelAbsensi)
        val tvStatus: TextView = view.findViewById(R.id.tvStatusAbsensi)
        val tvWaktu: TextView = view.findViewById(R.id.tvWaktuAbsensi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_absensi, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listAbsensi[position]
        holder.tvMapel.text = data.nama_mapel
        holder.tvStatus.text = data.status
        holder.tvWaktu.text = "${data.tanggal} (${data.jam_mulai.substring(0, 5)} - ${data.jam_selesai.substring(0, 5)})"

        when (data.status.lowercase()) {
            "hadir" -> {
                holder.tvStatus.setTextColor(Color.parseColor("#03543F"))
                holder.tvStatus.setBackgroundColor(Color.parseColor("#DEF7EC"))
            }
            "sakit", "izin" -> {
                holder.tvStatus.setTextColor(Color.parseColor("#92400E"))
                holder.tvStatus.setBackgroundColor(Color.parseColor("#FEF3C7"))
            }
            "alpa" -> {
                holder.tvStatus.setTextColor(Color.parseColor("#991B1B"))
                holder.tvStatus.setBackgroundColor(Color.parseColor("#FEE2E2"))
            }
        }
    }

    override fun getItemCount(): Int = listAbsensi.size
}
