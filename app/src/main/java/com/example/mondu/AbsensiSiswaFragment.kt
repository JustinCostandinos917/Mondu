package com.example.mondu

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class AbsensiSiswaFragment : Fragment() {

    private lateinit var rvAbsensi: RecyclerView
    private lateinit var pbAbsensi: ProgressBar
    private lateinit var tvEmpty: TextView
    private val listAbsensi = ArrayList<AbsensiSiswa>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_absensi_siswa, container, false)

        rvAbsensi = view.findViewById(R.id.rvAbsensiSiswa)
        pbAbsensi = view.findViewById(R.id.pbAbsensi)
        tvEmpty = view.findViewById(R.id.tvEmptyAbsensi)

        loadAbsensi()

        return view
    }

    private fun loadAbsensi() {
        val sharedPref = requireContext().getSharedPreferences("MonduSession", Context.MODE_PRIVATE)
        val idUser = sharedPref.getString("id_user", "") ?: ""

        if (idUser.isEmpty()) {
            tvEmpty.text = "Data sesi tidak ditemukan."
            tvEmpty.visibility = View.VISIBLE
            return
        }

        pbAbsensi.visibility = View.VISIBLE
        val url = "http://10.0.2.2/api_mondu/get_absensi_siswa.php?id_user=$idUser"

        val stringRequest = StringRequest(Request.Method.GET, url,
            { response ->
                pbAbsensi.visibility = View.GONE
                try {
                    val jsonObject = JSONObject(response)
                    val status = jsonObject.getString("status")
                    if (status == "success") {
                        val jsonArray = jsonObject.getJSONArray("data")
                        listAbsensi.clear()
                        for (i in 0 until jsonArray.length()) {
                            val item = jsonArray.getJSONObject(i)
                            listAbsensi.add(
                                AbsensiSiswa(
                                    item.getString("id_absensi"),
                                    item.getString("nama_mapel"),
                                    item.getString("tanggal"),
                                    item.getString("status"),
                                    item.getString("jam_mulai"),
                                    item.getString("jam_selesai")
                                )
                            )
                        }

                        if (listAbsensi.isEmpty()) {
                            tvEmpty.text = "Belum ada riwayat absensi."
                            tvEmpty.visibility = View.VISIBLE
                        } else {
                            tvEmpty.visibility = View.GONE
                            rvAbsensi.adapter = AbsensiSiswaAdapter(listAbsensi)
                        }
                    } else {
                        tvEmpty.text = jsonObject.getString("message")
                        tvEmpty.visibility = View.VISIBLE
                    }
                } catch (e: Exception) {
                    Log.e("AbsensiSiswa", "Error parsing: ${e.message}")
                    tvEmpty.text = "Gagal memuat absensi."
                    tvEmpty.visibility = View.VISIBLE
                }
            },
            { error ->
                pbAbsensi.visibility = View.GONE
                val errorMessage = error.networkResponse?.let {
                    "Error ${it.statusCode}: ${String(it.data)}"
                } ?: error.message ?: "Koneksi bermasalah."
                Log.e("AbsensiSiswa", "Error connection: $errorMessage")
                tvEmpty.text = "Koneksi bermasalah: $errorMessage"
                tvEmpty.visibility = View.VISIBLE
            }
        )

        Volley.newRequestQueue(requireContext()).add(stringRequest)
    }
}
