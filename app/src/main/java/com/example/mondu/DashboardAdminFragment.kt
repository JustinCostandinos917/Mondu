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
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class DashboardAdminFragment : Fragment() {
    private lateinit var tvTotalUsers: TextView
    private lateinit var tvTotalGuru: TextView
    private lateinit var tvTotalSiswa: TextView
    private lateinit var tvTotalKelas: TextView
    private lateinit var tvUserName: TextView
    private lateinit var btnLogout: MaterialCardView
    private lateinit var btnQuickAddUser: MaterialButton
    private lateinit var btnQuickLog: MaterialButton
    private lateinit var pieChart: PieChart

    private var countGuru = 0
    private var countSiswa = 0
    private var countTotalUser = 0
    private var countKelas = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard_admin, container, false)

        tvTotalUsers = view.findViewById(R.id.tvTotalUsers)
        tvTotalGuru = view.findViewById(R.id.tvTotalGuru)
        tvTotalSiswa = view.findViewById(R.id.tvTotalSiswa)
        tvTotalKelas = view.findViewById(R.id.tvTotalKelas)
        tvUserName = view.findViewById(R.id.tvUserNameDashboard)
        btnLogout = view.findViewById(R.id.btnLogout)
        btnQuickAddUser = view.findViewById(R.id.btnQuickAddUser)
        btnQuickLog = view.findViewById(R.id.btnQuickLog)
        pieChart = view.findViewById(R.id.pieChartStats)

        // Set User Profile Info
        val sharedPref = requireContext().getSharedPreferences("MonduSession", android.content.Context.MODE_PRIVATE)
        val namaLengkap = sharedPref.getString("nama_lengkap", "Admin Mondu")
        tvUserName.text = namaLengkap

        setupPieChart()

        // Load stats
        loadStats()

        btnLogout.setOnClickListener {
            showLogoutDialog()
        }

        btnQuickAddUser.setOnClickListener {
            startActivity(Intent(context, AddUserActivity::class.java))
        }

        btnQuickLog.setOnClickListener {
            (activity as? DashboardAdminActivity)?.setBottomNavigationItem(R.id.nav_logs)
        }

        return view
    }

    private fun setupPieChart() {
        pieChart.apply {
            setUsePercentValues(false) // Menampilkan angka asli, bukan persentase
            description.isEnabled = false
            setExtraOffsets(5f, 10f, 5f, 5f)
            dragDecelerationFrictionCoef = 0.95f
            isDrawHoleEnabled = true
            setHoleColor(android.graphics.Color.WHITE)
            transparentCircleRadius = 61f
            centerText = "Distribusi User"
            setCenterTextSize(16f)
            legend.isEnabled = true // Aktifkan legenda agar lebih rapi
            setEntryLabelColor(android.graphics.Color.BLACK) // Warna label di dalam slice
        }
    }

    private fun loadStats() {
        val queue = Volley.newRequestQueue(context)
        val baseUrl = "http://10.0.2.2/api_mondu/"

        // Fetch Total Users
        queue.add(JsonArrayRequest(Request.Method.GET, baseUrl + "get_users.php", null,
            { response -> 
                countTotalUser = response.length()
                tvTotalUsers.text = countTotalUser.toString() 
                updateChart()
            },
            { error -> Log.e("Dashboard", "Error users: ${error.message}") }
        ))

        // Fetch Total Guru
        queue.add(JsonArrayRequest(Request.Method.GET, baseUrl + "get_guru.php", null,
            { response -> 
                countGuru = response.length()
                tvTotalGuru.text = countGuru.toString()
                updateChart()
            },
            { error -> Log.e("Dashboard", "Error guru: ${error.message}") }
        ))

        // Fetch Total Siswa
        queue.add(JsonArrayRequest(Request.Method.GET, baseUrl + "get_siswa.php", null,
            { response -> 
                countSiswa = response.length()
                tvTotalSiswa.text = countSiswa.toString()
                updateChart()
            },
            { error -> Log.e("Dashboard", "Error siswa: ${error.message}") }
        ))

        // Fetch Total Kelas
        queue.add(JsonArrayRequest(Request.Method.GET, baseUrl + "get_kelas_akademik.php", null,
            { response -> 
                countKelas = response.length()
                tvTotalKelas.text = countKelas.toString() 
            },
            { error -> Log.e("Dashboard", "Error kelas: ${error.message}") }
        ))
    }

    private fun updateChart() {
        val entries = ArrayList<PieEntry>()
        if (countGuru > 0) entries.add(PieEntry(countGuru.toFloat(), "Guru"))
        if (countSiswa > 0) entries.add(PieEntry(countSiswa.toFloat(), "Siswa"))
        
        // Menghitung Admin (Total - Guru - Siswa)
        val countAdmin = countTotalUser - countGuru - countSiswa
        if (countAdmin > 0) entries.add(PieEntry(countAdmin.toFloat(), "Admin"))

        if (entries.isEmpty()) return

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        dataSet.valueTextSize = 14f
        dataSet.valueTextColor = android.graphics.Color.BLACK
        
        // Format angka agar tidak ada desimal (misal 5.0 jadi 5)
        dataSet.valueFormatter = object : com.github.mikephil.charting.formatter.ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return value.toInt().toString()
            }
        }

        val data = PieData(dataSet)
        pieChart.data = data
        pieChart.centerText = "Total\n$countTotalUser"
        pieChart.invalidate() // Refresh chart
    }

    private fun showLogoutDialog() {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Apakah Anda yakin ingin keluar?")
            .setPositiveButton("Ya") { _, _ ->
                performLogout()
            }
            .setNegativeButton("Tidak", null)
            .show()
    }

    private fun performLogout() {
        val sharedPref = requireContext().getSharedPreferences("MonduSession", android.content.Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.clear()
        editor.apply()

        Toast.makeText(context, "Berhasil keluar akun", Toast.LENGTH_SHORT).show()

        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        activity?.finish()
    }

    override fun onResume() {
        super.onResume()
        loadStats()
    }
}