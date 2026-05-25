package com.example.mondu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.google.android.material.card.MaterialCardView

class ArsipJenjangFragment : Fragment() {

    private var tahunAjaran: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tahunAjaran = it.getString(ARG_TAHUN)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_arsip_jenjang, container, false)

        val tvSubTitle = view.findViewById<TextView>(R.id.tvSubTitleJenjang)
        tvSubTitle.text = "Tahun Ajaran $tahunAjaran"

        val btnBack = view.findViewById<ImageButton>(R.id.btnBackJenjang)
        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val cardKelas7 = view.findViewById<MaterialCardView>(R.id.cardKelas7)
        val cardKelas8 = view.findViewById<MaterialCardView>(R.id.cardKelas8)
        val cardKelas9 = view.findViewById<MaterialCardView>(R.id.cardKelas9)

        cardKelas7.setOnClickListener { navigateToKelas("7") }
        cardKelas8.setOnClickListener { navigateToKelas("8") }
        cardKelas9.setOnClickListener { navigateToKelas("9") }

        return view
    }

    private fun navigateToKelas(jenjang: String) {
        val fragment = ArsipKelasFragment.newInstance(tahunAjaran ?: "", jenjang)
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

        @JvmStatic
        fun newInstance(tahun: String) =
            ArsipJenjangFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TAHUN, tahun)
                }
            }
    }
}