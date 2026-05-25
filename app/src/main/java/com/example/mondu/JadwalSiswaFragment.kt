package com.example.mondu

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class JadwalSiswaFragment : Fragment() {

    private lateinit var rvJadwal: RecyclerView
    private lateinit var pbJadwal: ProgressBar
    private lateinit var tvEmpty: TextView
    private val listJadwal = ArrayList<JadwalSiswa>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_jadwal_siswa, container, false)

        val layoutJadwal = view.findViewById<LinearLayout>(R.id.layoutJadwalHarian)
        val layoutKalender = view.findViewById<LinearLayout>(R.id.layoutFullKalender)
        val btnBukaKalender = view.findViewById<Button>(R.id.btnBukaKalender)
        val btnKembali = view.findViewById<Button>(R.id.btnKembaliKeJadwal)

        rvJadwal = view.findViewById(R.id.rvJadwalSiswa)
        pbJadwal = view.findViewById(R.id.pbJadwal)
        tvEmpty = view.findViewById(R.id.tvEmptyJadwal)

        // Setup LayoutManager
        rvJadwal.layoutManager = LinearLayoutManager(context)

        btnBukaKalender.setOnClickListener {
            layoutJadwal.visibility = View.GONE
            layoutKalender.visibility = View.VISIBLE
        }

        btnKembali.setOnClickListener {
            layoutKalender.visibility = View.GONE
            layoutJadwal.visibility = View.VISIBLE
        }

        loadJadwal()

        return view
    }

    private fun loadJadwal() {
        val sharedPref = requireContext().getSharedPreferences("MonduSession", Context.MODE_PRIVATE)
        val idUser = sharedPref.getString("id_user", "") ?: ""

        pbJadwal.visibility = View.VISIBLE
        
        // Use id_user if id_kelas is not in SharedPreferences yet
        val url = "http://10.0.2.2/api_mondu/get_jadwal_siswa.php?id_user=$idUser"

        val stringRequest = StringRequest(Request.Method.GET, url,
            { response ->
                pbJadwal.visibility = View.GONE
                try {
                    val jsonObject = JSONObject(response)
                    val status = jsonObject.getString("status")
                    if (status == "success") {
                        val jsonArray = jsonObject.getJSONArray("data")
                        listJadwal.clear()
                        for (i in 0 until jsonArray.length()) {
                            val item = jsonArray.getJSONObject(i)
                            val obj = item
                            val hariData = obj.getString("hari").trim() // .trim() membersihkan spasi tersembunyi
                            Log.d("DEBUG_DATA_HARI", "Hari ditemukan: '$hariData'")
                            listJadwal.add(
                                JadwalSiswa(
                                    item.getString("id_jadwal"),
                                    item.getString("nama_mapel"),
                                    item.getString("nama_guru"),
                                    item.getString("hari"),
                                    item.getString("jam_mulai"),
                                    item.getString("jam_selesai")
                                )
                            )
                        }

                        if (listJadwal.isEmpty()) {
                            val debugHari = jsonObject.optString("debug_hari", "Tidak diketahui")
                            tvEmpty.text = "Tidak ada jadwal untuk hari $debugHari."
                            tvEmpty.visibility = View.VISIBLE
                        } else {
                            tvEmpty.visibility = View.GONE
                            val urutanHari = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu")
                            val sortedList = ArrayList(listJadwal.sortedBy { urutanHari.indexOf(it.hari) })
                            rvJadwal.adapter = JadwalSiswaAdapter(sortedList)
                        }
                    } else {
                        tvEmpty.text = jsonObject.getString("message")
                        tvEmpty.visibility = View.VISIBLE
                    }
                } catch (e: Exception) {
                    Log.e("JadwalSiswa", "Error parsing: ${e.message}")
                    tvEmpty.text = "Gagal memuat jadwal."
                    tvEmpty.visibility = View.VISIBLE
                }
            },
            { error ->
                pbJadwal.visibility = View.GONE
                val errorMessage = error.networkResponse?.let {
                    "Error ${it.statusCode}: ${String(it.data)}"
                } ?: error.message ?: "Koneksi bermasalah."
                
                // Jika koneksi gagal (seperti 404), tampilkan data contoh (mock data)
                loadMockData()
            }
        )

        Volley.newRequestQueue(requireContext()).add(stringRequest)
    }

    private fun loadMockData() {
        listJadwal.clear()
        listJadwal.add(JadwalSiswa("1", "Matematika", "Budi Santoso, S.Pd", "Senin", "08:00", "09:30"))
        listJadwal.add(JadwalSiswa("2", "Bahasa Inggris", "Siti Aminah, M.Pd", "Senin", "09:45", "11:15"))
        listJadwal.add(JadwalSiswa("3", "Informatika", "Eko Prasetyo, S.Kom", "Senin", "11:30", "13:00"))
        
        rvJadwal.adapter = JadwalSiswaAdapter(listJadwal)
        tvEmpty.visibility = View.GONE
    }
}
