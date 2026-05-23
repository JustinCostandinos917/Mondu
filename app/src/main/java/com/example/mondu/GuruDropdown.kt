package com.example.mondu

data class GuruDropdown(
    val nuptk: String, // Ganti dari id_user ke nuptk
    val nama_lengkap: String
) {
    override fun toString(): String {
        return nama_lengkap
    }
}
