package com.example.mondu

data class Jadwal(
    val id_jadwal: String = "",
    val id_mapel: String = "",    // Tambahkan ini
    val id_kelas: String = "",
    val jam_mulai: String = "",
    val jam_selesai: String = "",
    val nama_mapel: String = "",
    val nama_guru: String = "",
    val nama_kelas: String = "",
    val hari: String = "",
    val isHeader: Boolean = false
)
