package com.example.mondu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LogAdapter(
    private var logList: List<LogActivity>,
    private val onItemClick: (LogActivity) -> Unit
) : RecyclerView.Adapter<LogAdapter.LogViewHolder>() {

    class LogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cvLogIcon: com.google.android.material.card.MaterialCardView = itemView.findViewById(R.id.cvLogIcon)
        val ivLogIcon: ImageView = itemView.findViewById(R.id.ivLogIcon)
        val tvLogTitle: TextView = itemView.findViewById(R.id.tvLogTitle)
        val tvLogDescription: TextView = itemView.findViewById(R.id.tvLogDescription)
        val tvLogTime: TextView = itemView.findViewById(R.id.tvLogTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_log, parent, false)
        return LogViewHolder(view)
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        val log = logList[position]
        holder.tvLogTitle.text = log.aksi
        holder.tvLogDescription.text = "${log.nama_user} • ${log.role}"
        holder.tvLogTime.text = log.waktu

        // Mengembalikan ke warna Biru Mondu (Warna Sebelumnya)
        holder.cvLogIcon.setCardBackgroundColor(android.graphics.Color.parseColor("#EFF6FF")) // Biru sangat muda
        holder.ivLogIcon.setColorFilter(android.graphics.Color.parseColor("#1E3A8A")) // Biru Tua Mondu

        // Ikon tetap dinamis sesuai aksi agar informatif
        when {
            log.aksi.contains("Tambah", true) -> holder.ivLogIcon.setImageResource(android.R.drawable.ic_menu_add)
            log.aksi.contains("Hapus", true) -> holder.ivLogIcon.setImageResource(android.R.drawable.ic_menu_delete)
            log.aksi.contains("Ubah", true) || log.aksi.contains("Edit", true) -> holder.ivLogIcon.setImageResource(android.R.drawable.ic_menu_edit)
            else -> holder.ivLogIcon.setImageResource(android.R.drawable.ic_menu_info_details)
        }

        holder.itemView.setOnClickListener { onItemClick(log) }
    }

    override fun getItemCount(): Int = logList.size

    fun updateData(newList: List<LogActivity>) {
        this.logList = newList
        notifyDataSetChanged()
    }
}
