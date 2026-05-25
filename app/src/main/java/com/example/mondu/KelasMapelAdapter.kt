package com.example.mondu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class KelasMapelAdapter(
    private val list: ArrayList<KelasMapel>,
    private val onItemClick: (KelasMapel) -> Unit
) : RecyclerView.Adapter<KelasMapelAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNamaKelas: TextView = view.findViewById(R.id.tvNamaKelas) // Sesuaikan ID di item_kelas_mapel.xml
        val tvNamaMapel: TextView = view.findViewById(R.id.tvNamaMapel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Pastikan kamu sudah buat file layout item_kelas_mapel.xml
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_kelas_mapel, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.tvNamaKelas.text = item.nama_kelas
        holder.tvNamaMapel.text = item.nama_mapel

        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    override fun getItemCount(): Int = list.size
}