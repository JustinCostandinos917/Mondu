package com.example.mondu

data class MapelDropdown(
    val id_mapel: String,
    val nama_mapel: String
) {
    override fun toString(): String {
        return nama_mapel // Agar dropdown menampilkan nama mata pelajaran
    }
}