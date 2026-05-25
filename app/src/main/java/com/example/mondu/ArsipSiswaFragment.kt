package com.example.mondu

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ArsipSiswaFragment : Fragment() {

    private var tahunAjaran: String? = null
    private var kelas: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tahunAjaran = it.getString(ARG_TAHUN)
            kelas = it.getString(ARG_KELAS)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_arsip_siswa, container, false)

        val tvSubTitle = view.findViewById<TextView>(R.id.tvSubTitleSiswa)
        tvSubTitle.text = "Rombel $kelas - $tahunAjaran"

        val btnBack = view.findViewById<ImageButton>(R.id.btnBackSiswa)
        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val rvSiswa = view.findViewById<RecyclerView>(R.id.rvSiswa)
        rvSiswa.layoutManager = LinearLayoutManager(context)

        // Mock data
        val listSiswa = listOf("Ahmad Fauzi", "Budi Santoso", "Citra Lestari", "Dewi Sartika")
        rvSiswa.adapter = SiswaArchiveAdapter(listSiswa) { selectedSiswa ->
            val intent = Intent(requireContext(), DetailRapotActivity::class.java)
            intent.putExtra("NAMA_SISWA", selectedSiswa)
            intent.putExtra("KELAS", kelas)
            intent.putExtra("TAHUN", tahunAjaran)
            startActivity(intent)
        }

        return view
    }

    companion object {
        private const val ARG_TAHUN = "tahun_ajaran"
        private const val ARG_KELAS = "kelas"

        @JvmStatic
        fun newInstance(tahun: String, kelas: String) =
            ArsipSiswaFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TAHUN, tahun)
                    putString(ARG_KELAS, kelas)
                }
            }
    }

    private class SiswaArchiveAdapter(
        private val items: List<String>,
        private val onClick: (String) -> Unit
    ) : RecyclerView.Adapter<SiswaArchiveAdapter.ViewHolder>() {

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvNamaSiswa: TextView = view.findViewById(R.id.tvArchiveItemName)
            val card: View = view
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_archive, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.tvNamaSiswa.text = items[position]
            holder.card.setOnClickListener { onClick(items[position]) }
        }

        override fun getItemCount() = items.size
    }
}