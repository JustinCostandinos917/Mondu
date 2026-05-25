package com.example.mondu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class JadwalSiswaAdapter(private val rawList: ArrayList<JadwalSiswa>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_HEADER = 0
    private val TYPE_ITEM = 1
    private val processedList = ArrayList<Any>() // Bisa berisi JadwalSiswa atau String (Header)

    init {
        groupDataByHari()
    }

    private fun groupDataByHari() {
        processedList.clear()
        var hariTerakhir = ""

        for (item in rawList) {
            if (item.hari != hariTerakhir) {
                hariTerakhir = item.hari
                processedList.add(hariTerakhir) // Tambahkan nama hari sebagai header
            }
            processedList.add(item) // Tambahkan objek jadwal
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (processedList[position] is String) TYPE_HEADER else TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_HEADER) {
            HeaderViewHolder(inflater.inflate(R.layout.item_header_hari, parent, false))
        } else {
            ItemViewHolder(inflater.inflate(R.layout.item_jadwal_siswa, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = processedList[position]
        if (holder is HeaderViewHolder) {
            holder.tvHeaderHari.text = (item as String).uppercase()
        } else if (holder is ItemViewHolder) {
            val jadwal = item as JadwalSiswa
            val jamMulai = if (jadwal.jam_mulai.length >= 5) jadwal.jam_mulai.substring(0, 5) else jadwal.jam_mulai
            val jamSelesai = if (jadwal.jam_selesai.length >= 5) jadwal.jam_selesai.substring(0, 5) else jadwal.jam_selesai
            holder.tvWaktuJadwal.text = "$jamMulai - $jamSelesai"
            holder.tvMapelJadwal.text = jadwal.nama_mapel
            holder.tvGuruJadwal.text = "Guru: ${jadwal.nama_guru}"
        }
    }

    override fun getItemCount(): Int = processedList.size

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvHeaderHari: TextView = view.findViewById(R.id.tvHeaderHari)
    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvWaktuJadwal: TextView = view.findViewById(R.id.tvWaktuJadwal)
        val tvMapelJadwal: TextView = view.findViewById(R.id.tvMapelJadwal)
        val tvGuruJadwal: TextView = view.findViewById(R.id.tvGuruJadwal)
    }
}
