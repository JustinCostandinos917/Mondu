package com.example.mondu

import android.content.Context
import android.content.Intent
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

class InputNilaiFragment : Fragment() {

    private lateinit var rvKelasMapel: RecyclerView
    private lateinit var adapter: KelasMapelAdapter // Kamu perlu buat adapter ini
    private val listKelasMapel = ArrayList<KelasMapel>() // Sesuaikan dengan data class kamu

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_input_nilai, container, false)

        rvKelasMapel = view.findViewById(R.id.rvKelasMapel)
        rvKelasMapel.layoutManager = LinearLayoutManager(activity)

        // Adapter untuk list kelas/mapel
        adapter = KelasMapelAdapter(listKelasMapel) { data ->
            // KETIKA DIPENCET -> PINDAH KE ACTIVITY
            val intent = Intent(requireContext(), InputNilaiAbsenActivity::class.java)
            intent.putExtra("id_mapel", data.id_mapel)
            intent.putExtra("id_kelas", data.id_kelas)
            intent.putExtra("nama_kelas", data.nama_kelas)
            intent.putExtra("nama_mapel", data.nama_mapel)
            startActivity(intent)
        }
        rvKelasMapel.adapter = adapter

        // Fetch data kelas/mapel yang diampu
        val prefs = requireActivity().getSharedPreferences("MonduSession", Context.MODE_PRIVATE)
        val nuptk = prefs.getString("nuptk", "") ?: ""
        bacaDataKelasMapel(nuptk)

        return view
    }

    private fun bacaDataKelasMapel(nuptk: String) {
        val url = "http://10.0.2.2/api_mondu/get_kelas_mapel.php?nuptk=$nuptk"
        val requestQueue = Volley.newRequestQueue(requireContext())
        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url, null,
            { response ->
                listKelasMapel.clear()
                for (i in 0 until response.length()) {
                    val obj = response.getJSONObject(i)
                    listKelasMapel.add(KelasMapel(
                        id_mapel = obj.getString("id_mapel"),
                        id_kelas = obj.getString("id_kelas"),
                        nama_mapel = obj.getString("nama_mapel"),
                        nama_kelas = obj.getString("nama_kelas")
                    ))
                }
                adapter.notifyDataSetChanged()
            },
            { error -> Toast.makeText(context, "Gagal: ${error.message}", Toast.LENGTH_SHORT).show() }
        )
        requestQueue.add(jsonArrayRequest)
    }
}