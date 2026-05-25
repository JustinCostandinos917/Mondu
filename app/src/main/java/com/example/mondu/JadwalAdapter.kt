package com.example.mondu

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class JadwalAdapter(
    private val listJadwal: List<Jadwal>,
    private val listener: OnJadwalClickListener
) : RecyclerView.Adapter<JadwalAdapter.ViewHolder>() {

    interface OnJadwalClickListener {
        fun onEditClick(jadwal: Jadwal)
        fun onDeleteClick(jadwal: Jadwal)
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Pastikan ID ini ada di item_jadwal.xml kamu
        val tvJam = view.findViewById<TextView>(R.id.tvJam)
        val tvMapel = view.findViewById<TextView>(R.id.tvMapel)
        val tvGuru = view.findViewById<TextView>(R.id.tvGuru)
        val btnEdit = view.findViewById<ImageButton>(R.id.btnEdit)
        val btnDelete = view.findViewById<ImageButton>(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Menghubungkan layout item_jadwal.xml ke adapter
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_jadwal, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listJadwal[position]
        Log.d("DEBUG_DATA", "Jam: ${item.jam_mulai} | Mapel: ${item.nama_mapel}")
        // Mengisi data ke TextView
        holder.tvJam.text = "${item.jam_mulai.substring(0,5)} - ${item.jam_selesai.substring(0,5)}"
        holder.tvMapel.text = item.nama_mapel
        holder.tvGuru.text = item.nama_guru
        holder.btnEdit.setOnClickListener { listener.onEditClick(item) }
        holder.btnDelete.setOnClickListener { listener.onDeleteClick(item) }
    }

    override fun getItemCount(): Int = listJadwal.size
}