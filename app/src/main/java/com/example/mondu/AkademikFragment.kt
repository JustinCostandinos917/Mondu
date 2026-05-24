package com.example.mondu

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley

class AkademikFragment : Fragment() {
    private lateinit var rvKelas: RecyclerView
    private val listKelas = ArrayList<HashMap<String, String>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_akademik, container, false)

        rvKelas = view.findViewById(R.id.rvKelas)
        rvKelas.layoutManager = LinearLayoutManager(context)

        // Ambil data kelas saat fragment dibuka
        loadDataKelas()

        return view
    }

    private fun loadDataKelas() {
        val url = "http://10.0.2.2/api_mondu/get_kelas_akademik.php"

        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    listKelas.clear()
                    for (i in 0 until response.length()) {
                        val obj = response.getJSONObject(i)
                        val map = HashMap<String, String>()
                        map["id_kelas"] = obj.getString("id_kelas")
                        map["nama_kelas"] = obj.getString("nama_kelas")
                        map["nama_wali"] = obj.getString("nama_wali")
                        listKelas.add(map)
                    }
                    // Pasang data ke RecyclerView lewat Adapter Custom Sederhana
                    setupRecyclerView()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            { error ->
                android.util.Log.e("VOLLEY_ERROR", "Penyebab: ${error.message} - ${error.networkResponse}")
                Toast.makeText(context, "Gagal mengambil data kelas", Toast.LENGTH_SHORT).show()
            })

        Volley.newRequestQueue(requireContext()).add(request)
    }

    class KelasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNamaKelas: TextView = itemView.findViewById(R.id.tvNamaKelasItem)
        val tvWaliKelas: TextView = itemView.findViewById(R.id.tvWaliKelasItem)
    }

    private fun setupRecyclerView() {
        rvKelas.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val v = LayoutInflater.from(parent.context).inflate(R.layout.item_kelas, parent, false)
                return KelasViewHolder(v)
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val item = listKelas[position]
                val h = holder as KelasViewHolder

                h.tvNamaKelas.text = item["nama_kelas"]
                h.tvWaliKelas.text = "Wali Kelas: ${item["nama_wali"]}"

                h.itemView.setOnClickListener {
                    tampilkanOpsiBottomSheet(item["id_kelas"]!!, item["nama_kelas"]!!)
                }
            }

            override fun getItemCount(): Int = listKelas.size
        }
    }


    private fun tampilkanOpsiBottomSheet(idKelas: String, namaKelas: String) {
        val bottomSheetDialog = com.google.android.material.bottomsheet.BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.layout_opsi_kelas, null)
        bottomSheetDialog.setContentView(view)

        view.findViewById<TextView>(R.id.tvJudulOpsiKelas).text = "Kelola Kelas $namaKelas"

        // Tombol Atur Wali Kelas
        view.findViewById<Button>(R.id.btnMenuWaliKelas).setOnClickListener {
            bottomSheetDialog.dismiss()
            val intent = Intent(context, AturWaliActivity::class.java).apply {
                putExtra("id_kelas", idKelas)
                putExtra("nama_kelas", namaKelas)
            }
            startActivity(intent)
        }

        // Tombol Tambah Jadwal
        view.findViewById<Button>(R.id.btnMenuTambahJadwal).setOnClickListener {
            bottomSheetDialog.dismiss()
            val intent = Intent(context,  AddJadwalActivity::class.java).apply {
                putExtra("id_kelas", idKelas)
                putExtra("nama_kelas", namaKelas)
            }
            startActivity(intent)
        }
        bottomSheetDialog.show()
    }
    override fun onResume() {
        super.onResume()
        loadDataKelas()
    }
}