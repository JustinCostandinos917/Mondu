package com.example.mondu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ArsipKelasFragment : Fragment() {

    private var tahunAjaran: String? = null
    private var jenjang: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tahunAjaran = it.getString(ARG_TAHUN)
            jenjang = it.getString(ARG_JENJANG)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_arsip_kelas, container, false)

        val tvSubTitle = view.findViewById<TextView>(R.id.tvSubTitleKelas)
        tvSubTitle.text = "Kelas $jenjang - $tahunAjaran"

        val btnBack = view.findViewById<ImageButton>(R.id.btnBackKelas)
        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val rvKelas = view.findViewById<RecyclerView>(R.id.rvKelas)
        rvKelas.layoutManager = LinearLayoutManager(context)

        // Mock data
        val listKelas = listOf("${jenjang}A", "${jenjang}B", "${jenjang}C")
        rvKelas.adapter = KelasArchiveAdapter(listKelas) { selectedKelas ->
            navigateToSiswa(selectedKelas)
        }

        return view
    }

    private fun navigateToSiswa(kelas: String) {
        val fragment = ArsipSiswaFragment.newInstance(tahunAjaran ?: "", kelas)
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    companion object {
        private const val ARG_TAHUN = "tahun_ajaran"
        private const val ARG_JENJANG = "jenjang"

        @JvmStatic
        fun newInstance(tahun: String, jenjang: String) =
            ArsipKelasFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TAHUN, tahun)
                    putString(ARG_JENJANG, jenjang)
                }
            }
    }

    // Inner Adapter for simplicity or create a separate file
    private class KelasArchiveAdapter(
        private val items: List<String>,
        private val onClick: (String) -> Unit
    ) : RecyclerView.Adapter<KelasArchiveAdapter.ViewHolder>() {

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvNamaKelas: TextView = view.findViewById(R.id.tvArchiveItemName)
            val card: View = view
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_archive, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.tvNamaKelas.text = items[position]
            holder.card.setOnClickListener { onClick(items[position]) }
        }

        override fun getItemCount() = items.size
    }
}