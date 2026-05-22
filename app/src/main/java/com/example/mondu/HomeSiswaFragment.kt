package com.example.mondu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView

class HomeSiswaFragment : Fragment() {

    private var asalHalaman = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_siswa, container, false)

        val lTampilan1 = view.findViewById<LinearLayout>(R.id.layoutTampilan1Home)
        val lTampilan2 = view.findViewById<LinearLayout>(R.id.layoutTampilan2FullList)
        val lTampilan3 = view.findViewById<LinearLayout>(R.id.layoutTampilan3Detail)

        val btnLihatSemua = view.findViewById<TextView>(R.id.btnLihatSemuaKontainer)
        val itemRingkas1 = view.findViewById<RelativeLayout>(R.id.itemRingkas1)
        val itemRingkas2 = view.findViewById<RelativeLayout>(R.id.itemRingkas2)

        val btnBackT2ToHome = view.findViewById<Button>(R.id.btnBackTampilan2ToHome)
        val itemFull1 = view.findViewById<RelativeLayout>(R.id.itemFull1)

        val btnBackT3ToPrev = view.findViewById<Button>(R.id.btnBackTampilan3ToPrevious)
        val tvJudul = view.findViewById<TextView>(R.id.tvDetailJudul)
        val tvTanggal = view.findViewById<TextView>(R.id.tvDetailTanggal)
        val tvPengirim = view.findViewById<TextView>(R.id.tvDetailPengirim) // KOMPONEN BARU

        // Hubungkan Navigasi Utama
        btnLihatSemua.setOnClickListener {
            lTampilan1.visibility = View.GONE
            lTampilan2.visibility = View.VISIBLE
        }

        btnBackT2ToHome.setOnClickListener {
            lTampilan2.visibility = View.GONE
            lTampilan1.visibility = View.VISIBLE
        }

        // -----------------------------------------------------------------
        // A. KLIK DARI TAMPILAN 1 (HOME RINGKAS)
        // -----------------------------------------------------------------
        itemRingkas1.setOnClickListener {
            asalHalaman = 1
            tvJudul.text = "Pengumuman Nilai UTS Matematika"
            tvPengirim.text = "Oleh: Budi Setiawan, S.Pd (Guru)"
            tvTanggal.text = "Tanggal: 22 Mei 2026"

            lTampilan1.visibility = View.GONE
            lTampilan3.visibility = View.VISIBLE
        }

        itemRingkas2.setOnClickListener {
            asalHalaman = 1
            tvJudul.text = "Surat Edaran Libur Semester Ganjil"
            tvPengirim.text = "Oleh: Sekretariat Tata Usaha (Admin)"
            tvTanggal.text = "Tanggal: 20 Mei 2026"

            lTampilan1.visibility = View.GONE
            lTampilan3.visibility = View.VISIBLE
        }

        // -----------------------------------------------------------------
        // B. KLIK DARI TAMPILAN 2 (FULL LIST)
        // -----------------------------------------------------------------
        itemFull1.setOnClickListener {
            asalHalaman = 2
            tvJudul.text = "Pengumuman Nilai UTS Matematika"
            tvPengirim.text = "Oleh: Budi Setiawan, S.Pd (Guru)"
            tvTanggal.text = "Tanggal: 22 Mei 2026"

            lTampilan2.visibility = View.GONE
            lTampilan3.visibility = View.VISIBLE
        }

        // Kembali dari Detail
        btnBackT3ToPrev.setOnClickListener {
            lTampilan3.visibility = View.GONE
            if (asalHalaman == 1) {
                lTampilan1.visibility = View.VISIBLE
            } else {
                lTampilan2.visibility = View.VISIBLE
            }
        }

        return view
    }
}