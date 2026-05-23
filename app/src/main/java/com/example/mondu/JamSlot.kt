package com.example.mondu

data class JamSlot(
    val jamMulai: String,
    val jamSelesai: String,
    var idMapel: String = "",
    var namaMapel: String = "",
    var namaGuru: String = ""
)
