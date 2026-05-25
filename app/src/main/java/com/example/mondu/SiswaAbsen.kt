package com.example.mondu

data class SiswaAbsen(
    val nis: String,
    val nama_lengkap: String,
    var status: String = "Hadir" // Default status
)
