package com.example.mondu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SiswaAdapter(
    private val listSiswa: List<Siswa>,
    private val onItemClick: (Siswa) -> Unit
) : RecyclerView.Adapter<SiswaAdapter.SiswaViewHolder>() {

    class SiswaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama: TextView = view.findViewById(R.id.tvNamaSiswa)
        val tvNis: TextView = view.findViewById(R.id.tvNis)
        val tvInisial: TextView = view.findViewById(R.id.tvInisial)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SiswaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_siswa, parent, false)
        return SiswaViewHolder(view)
    }

    override fun onBindViewHolder(holder: SiswaViewHolder, position: Int) {
        val siswa = listSiswa[position]

        holder.tvNama.text = siswa.nama
        holder.tvNis.text = "NIS: ${siswa.nis}"

        // Mengambil huruf pertama nama sebagai inisial
        holder.tvInisial.text = siswa.nama.first().toString().uppercase()

        // Handle klik
        holder.itemView.setOnClickListener {
            onItemClick(siswa)
        }
    }

    override fun getItemCount(): Int = listSiswa.size
}