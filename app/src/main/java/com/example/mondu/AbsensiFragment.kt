package com.example.mondu

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class AbsensiFragment : Fragment() {

    private lateinit var rvAbsensi: RecyclerView
    private var currentIdJadwal: String = ""
    private var currentIdKelas: String = "" // Tambahkan variabel ini

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_absensi, container, false)
        rvAbsensi = view.findViewById(R.id.rvAbsensi)
        val btnSimpan = view.findViewById<Button>(R.id.btnSimpanAbsen)

        // AMBIL DARI ARGUMENTS (Data kiriman Activity)
        currentIdJadwal = arguments?.getString("id_jadwal") ?: ""
        currentIdKelas = arguments?.getString("id_kelas") ?: ""

        rvAbsensi.layoutManager = LinearLayoutManager(context)

        btnSimpan.setOnClickListener {
            val adapter = rvAbsensi.adapter as? AbsensiAdapter
            if (adapter != null && currentIdJadwal.isNotEmpty()) {
                simpanAbsensi(adapter.getListSiswa(), currentIdJadwal)
            } else {
                Toast.makeText(context, "Data belum lengkap atau ID Jadwal kosong!", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // GUNAKAN VARIABEL YANG SUDAH DIAMBIL TADI
        if (currentIdKelas.isNotEmpty()) {
            loadDataSiswa(currentIdKelas)
        } else {
            Toast.makeText(context, "ID Kelas tidak ditemukan!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadDataSiswa(idKelas: String) {
        val url = "http://10.0.2.2/api_mondu/get_siswa_by_kelas.php?id_kelas=$idKelas"
        val stringRequest = StringRequest(Request.Method.GET, url, { response ->
            val jsonObject = JSONObject(response)
            val jsonArray = jsonObject.getJSONArray("data")
            val list = mutableListOf<SiswaAbsen>()

            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(i)
                list.add(SiswaAbsen(item.getString("nis"), item.getString("nama_lengkap")))
            }
            rvAbsensi.adapter = AbsensiAdapter(list)
        }, { error ->
            Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
        })
        Volley.newRequestQueue(context).add(stringRequest)
    }

    private fun simpanAbsensi(listSiswa: List<SiswaAbsen>, idJadwal: String) {
        val url = "http://10.0.2.2/api_mondu/simpan_absensi.php"
        val jsonArray = JSONArray()
        for (siswa in listSiswa) {
            val obj = JSONObject()
            obj.put("nis", siswa.nis)
            obj.put("status", siswa.status)
            jsonArray.put(obj)
        }

        val requestBody = JSONObject()
        requestBody.put("id_jadwal", idJadwal)
        requestBody.put("absensi", jsonArray)

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, requestBody,
            { response ->
                Toast.makeText(context, "Berhasil disimpan!", Toast.LENGTH_SHORT).show()
            }, { error ->
                Toast.makeText(context, "Gagal: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )
        Volley.newRequestQueue(context).add(jsonObjectRequest)
    }
}