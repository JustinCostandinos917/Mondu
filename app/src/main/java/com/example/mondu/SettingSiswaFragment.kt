package com.example.mondu

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingSiswaFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_setting_siswa, container, false)

        // Inisialisasi Komponen (Wajib pakai view.findViewById karena di dalam Fragment)
        val btnGantiPassword = view.findViewById<MaterialButton>(R.id.btn_ganti_password)
        val btnHubungiKami = view.findViewById<MaterialButton>(R.id.btn_hubungi_kami)
        val btnTentangAplikasi = view.findViewById<MaterialButton>(R.id.btn_tentang_aplikasi)
        val btnLogout = view.findViewById<MaterialButton>(R.id.btn_logout)
        val switchNotifikasi = view.findViewById<SwitchMaterial>(R.id.switch_notifikasi)

        // Listener Tombol Ganti Password (Pindah ke GantiPasswordActivity)
        btnGantiPassword.setOnClickListener {
            val intent = Intent(requireContext(), GantiPasswordActivity::class.java)
            startActivity(intent)
        }

        // Listener Tombol Hubungi Kami (Pindah ke ContactUsActivity)
        btnHubungiKami.setOnClickListener {
            val intent = Intent(requireContext(), ContactUsActivity::class.java)
            startActivity(intent)
        }

        // Listener Tombol Tentang Aplikasi
        btnTentangAplikasi.setOnClickListener {
            Toast.makeText(requireContext(), "Mondu App v1.0", Toast.LENGTH_SHORT).show()
        }

        // Listener Tombol Logout
        btnLogout.setOnClickListener {
            val sharedPref = requireContext().getSharedPreferences("MonduSession", android.content.Context.MODE_PRIVATE)

            val editor = sharedPref.edit()

            // 2. Bersihkan semua data login (id_user, username, isLoggedIn, dll)
            editor.clear()
            editor.apply()

            Toast.makeText(requireContext(), "Berhasil keluar akun", Toast.LENGTH_SHORT).show()

            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        // Listener Switch Notifikasi
        switchNotifikasi.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(requireContext(), "Notifikasi Diaktifkan", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Notifikasi Dimatikan", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }
}