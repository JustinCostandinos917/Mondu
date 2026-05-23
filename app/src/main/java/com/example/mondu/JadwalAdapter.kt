package com.example.mondu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
class JadwalAdapter(
    private val listJadwal: ArrayList<JamSlot>,
    private val onClick: (JamSlot) -> Unit
) : RecyclerView.Adapter<JadwalAdapter.ViewHolder>() {

    // 2. ViewHolder menghubungkan ID dari item_jadwal.xml
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtJam: TextView = view.findViewById(R.id.txtJam)
        val txtInfo: TextView = view.findViewById(R.id.txtInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_jadwal, parent, false) // Menggunakan layout item_jadwal.xml
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listJadwal[position]

        // Menampilkan rentang jam
        holder.txtJam.text = "${data.jamMulai.substring(0, 5)} - ${data.jamSelesai.substring(0, 5)}"

        // 3. Menampilkan isi jadwal jika sudah ada, atau teks instruksi jika kosong
        if (data.namaMapel.isEmpty()) {
            holder.txtInfo.text = "Klik untuk isi jadwal"
        } else {
            holder.txtInfo.text = "${data.namaMapel} (${data.namaGuru})"
        }

        // 4. Menangani klik baris
        holder.itemView.setOnClickListener {
            onClick(data)
        }
    }

    override fun getItemCount(): Int = listJadwal.size
}