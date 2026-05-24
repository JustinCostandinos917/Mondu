package com.example.mondu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LogAdapter(
    private var logList: List<LogActivity>
) : RecyclerView.Adapter<LogAdapter.LogViewHolder>() {

    class LogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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

        // Set icon based on action or role
        when (log.role.lowercase()) {
            "admin" -> holder.ivLogIcon.setImageResource(android.R.drawable.ic_lock_power_off)
            "guru" -> holder.ivLogIcon.setImageResource(android.R.drawable.ic_menu_edit)
            else -> holder.ivLogIcon.setImageResource(android.R.drawable.ic_menu_info_details)
        }
    }

    override fun getItemCount(): Int = logList.size

    fun updateData(newList: List<LogActivity>) {
        this.logList = newList
        notifyDataSetChanged()
    }
}
