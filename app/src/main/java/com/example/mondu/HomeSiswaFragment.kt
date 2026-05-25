package com.example.mondu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import com.google.android.material.button.MaterialButton

class HomeSiswaFragment : Fragment() {

    private var asalHalaman = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_siswa, container, false)

        // Tampilan Containers
        val lTampilan1 = view.findViewById<NestedScrollView>(R.id.layoutTampilan1Home)
        val lTampilan2 = view.findViewById<LinearLayout>(R.id.layoutTampilan2FullList)
        val lTampilan3 = view.findViewById<LinearLayout>(R.id.layoutTampilan3Detail)

        // Header & Session Data
        val tvNamaSiswa = view.findViewById<TextView>(R.id.tvNamaSiswaHeader)
        val sharedPref = requireContext().getSharedPreferences("MonduSession", Context.MODE_PRIVATE)
        val namaLengkap = sharedPref.getString("nama_lengkap", "Siswa Mondu")
        tvNamaSiswa.text = namaLengkap

        // Buttons & Menu
        val btnLihatSemua = view.findViewById<TextView>(R.id.btnLihatSemuaKontainer)
        
        val menuJadwal = view.findViewById<LinearLayout>(R.id.menuJadwal)
        val menuNilai = view.findViewById<LinearLayout>(R.id.menuNilai)

        val itemRingkas1 = view.findViewById<RelativeLayout>(R.id.itemRingkas1)
        val itemRingkas2 = view.findViewById<RelativeLayout>(R.id.itemRingkas2)

        val btnBackT2ToHome = view.findViewById<ImageButton>(R.id.btnBackTampilan2ToHome)
        val btnBackT3ToPrev = view.findViewById<ImageButton>(R.id.btnBackTampilan3ToPrevious)

        // Detail Views
        val tvJudul = view.findViewById<TextView>(R.id.tvDetailJudul)
        val tvTanggal = view.findViewById<TextView>(R.id.tvDetailTanggal)
        val tvPengirim = view.findViewById<TextView>(R.id.tvDetailPengirim)
        val tvKonten = view.findViewById<TextView>(R.id.tvDetailKonten)

        // Navigation Actions
        btnLihatSemua.setOnClickListener {
            lTampilan1.visibility = View.GONE
            lTampilan2.visibility = View.VISIBLE
        }

        btnBackT2ToHome.setOnClickListener {
            lTampilan2.visibility = View.GONE
            lTampilan1.visibility = View.VISIBLE
        }

        btnBackT3ToPrev.setOnClickListener {
            lTampilan3.visibility = View.GONE
            if (asalHalaman == 1) lTampilan1.visibility = View.VISIBLE else lTampilan2.visibility = View.VISIBLE
        }

        // Menu Click Listeners
        menuJadwal.setOnClickListener { Toast.makeText(context, "Membuka Jadwal...", Toast.LENGTH_SHORT).show() }
        menuNilai.setOnClickListener { Toast.makeText(context, "Membuka Nilai...", Toast.LENGTH_SHORT).show() }

        // Mock Detail Interactions
        itemRingkas1.setOnClickListener {
            asalHalaman = 1
            showDetail(tvJudul, tvPengirim, tvTanggal, tvKonten, 
                "Pengumuman Nilai UTS Matematika", 
                "Budi Setiawan • Guru", 
                "22 Mei 2026",
                "Nilai UTS Matematika sudah dapat dilihat di menu Nilai. Silakan cek hasil belajar kalian masing-masing.")
            lTampilan1.visibility = View.GONE
            lTampilan3.visibility = View.VISIBLE
        }

        itemRingkas2.setOnClickListener {
            asalHalaman = 1
            showDetail(tvJudul, tvPengirim, tvTanggal, tvKonten, 
                "Surat Edaran Libur Sekolah", 
                "Admin Tata Usaha • Admin", 
                "20 Mei 2026",
                "Diberitahukan kepada seluruh siswa bahwa kegiatan belajar mengajar akan diliburkan mulai tanggal 1 Juni dalam rangka libur semester.")
            lTampilan1.visibility = View.GONE
            lTampilan3.visibility = View.VISIBLE
        }

        return view
    }

    private fun showDetail(tvJ: TextView, tvP: TextView, tvT: TextView, tvK: TextView, judul: String, pengirim: String, tgl: String, konten: String) {
        tvJ.text = judul
        tvP.text = pengirim
        tvT.text = tgl
        tvK.text = konten
    }
}