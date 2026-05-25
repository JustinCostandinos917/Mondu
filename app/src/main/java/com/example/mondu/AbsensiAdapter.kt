package com.example.mondu

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AbsensiAdapter(private val listSiswa: List<SiswaAbsen>) : RecyclerView.Adapter<AbsensiAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama: TextView = view.findViewById(R.id.tvNamaSiswa)
        val radioGroup: RadioGroup = view.findViewById(R.id.rgStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_siswa_absen, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val siswa = listSiswa[position]
        holder.tvNama.text = siswa.nama_lengkap

        holder.radioGroup.setOnCheckedChangeListener(null)

        when (siswa.status) {
            "Hadir" -> holder.radioGroup.check(R.id.rbHadir)
            "Sakit" -> holder.radioGroup.check(R.id.rbSakit)
            "Izin" -> holder.radioGroup.check(R.id.rbIzin)
            "Alpha" -> holder.radioGroup.check(R.id.rbAlpha)
            else -> holder.radioGroup.clearCheck()
        }

        holder.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val status = when(checkedId) {
                R.id.rbHadir -> "Hadir"
                R.id.rbSakit -> "Sakit"
                R.id.rbIzin -> "Izin"
                else -> "Alpha"
            }
            siswa.status = status
        }
    }
    fun getListSiswa(): List<SiswaAbsen> {
        return listSiswa
    }

    override fun getItemCount(): Int = listSiswa.size
}
