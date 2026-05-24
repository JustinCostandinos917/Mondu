package com.example.mondu

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

class JadwalFragment(private val hari: String) : Fragment() {
    private lateinit var rvJadwal: RecyclerView
    private val listJadwal = ArrayList<Jadwal>() // Pastikan kamu punya data class Jadwal

    override fun onResume() {
        super.onResume()
        // Fungsi ini akan dijalankan otomatis saat kamu kembali dari layar Tambah Jadwal
        loadJadwalDariServer()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_jadwal, container, false)
        rvJadwal = view.findViewById(R.id.rvJadwal)
        rvJadwal.layoutManager = LinearLayoutManager(context)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadJadwalDariServer()
    }

    private fun loadJadwalDariServer() {
        val idKelas = JadwalActivity.idKelasDipilih
        val url = "http://10.0.2.2/api_mondu/get_jadwal.php?hari=$hari&id_kelas=$idKelas"

        val request = StringRequest(Request.Method.GET, url, { response ->
            val jsonArray = JSONArray(response)
            listJadwal.clear()
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                listJadwal.add(Jadwal(
                    obj.getString("id_jadwal"),
                    obj.getString("jam_mulai"),
                    obj.getString("jam_selesai"),
                    obj.getString("nama_mapel"),
                    obj.getString("nama_guru")
                ))
            }

            // PERBAIKAN DI SINI: Masukkan 'this' sebagai listener
            rvJadwal.adapter = JadwalAdapter(listJadwal, object : JadwalAdapter.OnJadwalClickListener {
                override fun onEditClick(jadwal: Jadwal) {
                    val intent = Intent(context, EditJadwalActivity::class.java)
                    intent.putExtra("ID_JADWAL", jadwal.id_jadwal)
                    intent.putExtra("ID_KELAS", JadwalActivity.idKelasDipilih) // Mengambil dari variabel statis yang kamu punya
                    intent.putExtra("HARI", hari)
                    startActivity(intent)
                }

                override fun onDeleteClick(jadwal: Jadwal) {
                    hapusJadwalDariDatabase(jadwal.id_jadwal)
                }
                private fun hapusJadwalDariDatabase(idJadwal: String) {
                    val url = "http://10.0.2.2/api_mondu/delete_jadwal.php?id_jadwal=$idJadwal"

                    val request = StringRequest(Request.Method.GET, url, { response ->
                        // Jika sukses, reload data agar tampilan terupdate
                        loadJadwalDariServer()
                    }, {
                        it.printStackTrace()
                    })

                    Volley.newRequestQueue(requireContext()).add(request)
                }
            })
        }, { it.printStackTrace() })

        Volley.newRequestQueue(requireContext()).add(request)
    }
}