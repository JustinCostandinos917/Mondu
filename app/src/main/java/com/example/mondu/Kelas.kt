package com.example.mondu

data class Kelas(
    val id_kelas: String,
    val nama_kelas: String
) {
    // Trik penting: Override toString() agar Dropdown/AutoCompleteTextView
    // otomatis menampilkan teks "7A" saja, bukan alamat memori objeknya.
    override fun toString(): String {
        return nama_kelas
    }
}
