package com.example.mondu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class JadwalGuruAdapter(private val rawList: ArrayList<Jadwal>, private val onItemClick: (Jadwal) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_HEADER = 0
    private val TYPE_ITEM = 1
    private val processedList = ArrayList<Jadwal>()

    init {
        groupDataByHari()
    }

    // FUNGSI BARU: Untuk memperbarui data secara dinamis dari fragment
    fun updateData(newList: List<Jadwal>) {
        rawList.clear()
        rawList.addAll(newList)
        groupDataByHari() // Proses ulang pengelompokan harinya
        notifyDataSetChanged() // Paksa RecyclerView gambar ulang layar
    }

    // Fungsi otomatis untuk menyelipkan Header Hari ke dalam List
    private fun groupDataByHari() {
        processedList.clear()
        var hariTerakhir = ""

        for (item in rawList) {
            // Jika harinya berubah, sisipkan objek khusus sebagai Header
            if (item.hari != hariTerakhir) {
                hariTerakhir = item.hari
                processedList.add(Jadwal(hari = hariTerakhir, isHeader = true))
            }
            processedList.add(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (processedList[position].isHeader) TYPE_HEADER else TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_HEADER) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_header_hari, parent, false)
            HeaderViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_jadwal_guru, parent, false)
            ItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = processedList[position]

        if (holder is HeaderViewHolder) {
            holder.tvHeaderHari.text = item.hari.uppercase()
        } else if (holder is ItemViewHolder) {
            holder.tvJam.text = "${item.jam_mulai} - ${item.jam_selesai}"
            holder.tvMapelKelas.text = "${item.nama_mapel} - ${item.nama_kelas}"
            holder.itemView.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    override fun getItemCount(): Int = processedList.size

    // ViewHolder untuk Judul Hari
    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvHeaderHari: TextView = view.findViewById(R.id.tvHeaderHari)
    }

    // ViewHolder untuk Card Isi Jadwal
    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvJam: TextView = view.findViewById(R.id.tvJam)
        val tvMapelKelas: TextView = view.findViewById(R.id.tvMapelKelas)
    }
}