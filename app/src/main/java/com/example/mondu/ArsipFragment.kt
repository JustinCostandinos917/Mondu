package com.example.mondu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.card.MaterialCardView

class ArsipFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_arsip, container, false)

        val card2026 = view.findViewById<MaterialCardView>(R.id.cardTahun2026)
        val card2025 = view.findViewById<MaterialCardView>(R.id.cardTahun2025)

        card2026.setOnClickListener {
            navigateToJenjang("2025/2026")
        }

        card2025.setOnClickListener {
            navigateToJenjang("2024/2025")
        }

        return view
    }

    private fun navigateToJenjang(tahun: String) {
        val fragment = ArsipJenjangFragment.newInstance(tahun)
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
}