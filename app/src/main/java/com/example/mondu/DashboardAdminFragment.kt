package com.example.mondu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.button.MaterialButton

class DashboardAdminFragment : Fragment() {
    private lateinit var tvTotalUsers: TextView
    private lateinit var tvTotalGuru: TextView
    private lateinit var tvTotalSiswa: TextView
    private lateinit var tvTotalKelas: TextView
    private lateinit var btnQuickAddUser: MaterialButton
    private lateinit var btnQuickLog: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard_admin, container, false)

        tvTotalUsers = view.findViewById(R.id.tvTotalUsers)
        tvTotalGuru = view.findViewById(R.id.tvTotalGuru)
        tvTotalSiswa = view.findViewById(R.id.tvTotalSiswa)
        tvTotalKelas = view.findViewById(R.id.tvTotalKelas)
        btnQuickAddUser = view.findViewById(R.id.btnQuickAddUser)
        btnQuickLog = view.findViewById(R.id.btnQuickLog)

        // Load stats
        loadStats()

        btnQuickAddUser.setOnClickListener {
            startActivity(Intent(context, AddUserActivity::class.java))
        }

        btnQuickLog.setOnClickListener {
            (activity as? DashboardAdminActivity)?.setBottomNavigationItem(R.id.nav_logs)
        }

        return view
    }

    private fun loadStats() {
        val queue = Volley.newRequestQueue(context)
        val baseUrl = "http://10.0.2.2/api_mondu/"

        // Fetch Total Users
        queue.add(JsonArrayRequest(Request.Method.GET, baseUrl + "get_users.php", null,
            { response -> tvTotalUsers.text = response.length().toString() },
            { error -> Log.e("Dashboard", "Error users: ${error.message}") }
        ))

        // Fetch Total Guru
        queue.add(JsonArrayRequest(Request.Method.GET, baseUrl + "get_guru.php", null,
            { response -> tvTotalGuru.text = response.length().toString() },
            { error -> Log.e("Dashboard", "Error guru: ${error.message}") }
        ))

        // Fetch Total Siswa
        queue.add(JsonArrayRequest(Request.Method.GET, baseUrl + "get_siswa.php", null,
            { response -> tvTotalSiswa.text = response.length().toString() },
            { error -> Log.e("Dashboard", "Error siswa: ${error.message}") }
        ))

        // Fetch Total Kelas
        queue.add(JsonArrayRequest(Request.Method.GET, baseUrl + "get_kelas_akademik.php", null,
            { response -> tvTotalKelas.text = response.length().toString() },
            { error -> Log.e("Dashboard", "Error kelas: ${error.message}") }
        ))
    }

    override fun onResume() {
        super.onResume()
        loadStats()
    }
}