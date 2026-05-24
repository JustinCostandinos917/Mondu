package com.example.mondu

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class JadwalGuruFragment : Fragment() {

    private lateinit var rvJadwal: RecyclerView
    private lateinit var adapter: JadwalGuruAdapter
    private val listJadwal = ArrayList<Jadwal>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_jadwal_guru, container, false)

        rvJadwal = view.findViewById(R.id.rvJadwal)
        rvJadwal.layoutManager = LinearLayoutManager(activity)

        // PERBAIKAN UTAMA: Tempel adapter kosong di sini sejak awal biar Android tidak mengeluh!
        adapter = JadwalGuruAdapter(listJadwal)
        rvJadwal.adapter = adapter

        val prefs = requireActivity().getSharedPreferences("MonduSession", Context.MODE_PRIVATE)
        val nuptk = prefs.getString("nuptk", "") ?: ""

        val sdf = SimpleDateFormat("EEEE", Locale("id", "ID"))
        val hariIni = sdf.format(Calendar.getInstance().time)

        if (nuptk.isNotEmpty()) {
            bacaDataJadwal(nuptk, hariIni)
        } else {
            Toast.makeText(activity, "Sesi login tidak ditemukan!", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun bacaDataJadwal(nuptk: String, hari: String) {
        val url = "http://10.0.2.2/api_mondu/get_jadwal.php?nuptk=$nuptk"

        val requestQueue = Volley.newRequestQueue(activity)
        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url, null,
            { response ->
                try {
                    // Buat list temporer untuk menampung hasil fetch terbaru
                    val tempList = ArrayList<Jadwal>()

                    for (i in 0 until response.length()) {
                        val obj = response.getJSONObject(i)

                        if (obj.has("error")) {
                            Toast.makeText(activity, obj.getString("error"), Toast.LENGTH_LONG).show()
                            return@JsonArrayRequest
                        }

                        val data = Jadwal(
                            id_jadwal = obj.getString("id_jadwal"),
                            jam_mulai = obj.getString("jam").substringBefore(" - "),
                            jam_selesai = obj.getString("jam").substringAfter(" - "),
                            nama_mapel = obj.getString("nama_mapel"),
                            nama_kelas = obj.getString("nama_kelas"),
                            hari = obj.getString("hari"),
                            nama_guru = ""
                        )
                        tempList.add(data)
                    }

                    // PERBAIKAN KEDUA: Suntikkan data baru ke adapter yang sudah terpasang
                    adapter.updateData(tempList)

                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(activity, "Error parsing data: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Toast.makeText(activity, "Gagal terhubung ke server: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )
        requestQueue.add(jsonArrayRequest)
    }
}