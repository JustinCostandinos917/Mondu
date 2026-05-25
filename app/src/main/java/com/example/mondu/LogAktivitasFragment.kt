package com.example.mondu

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject

class LogAktivitasFragment : Fragment() {
    private lateinit var rvLogs: RecyclerView
    private lateinit var etSearchLogs: TextInputEditText
    private lateinit var btnFilterLogs: MaterialCardView
    private lateinit var logAdapter: LogAdapter
    private var logList = ArrayList<LogActivity>()
    private var fullLogList = ArrayList<LogActivity>()
    private var currentFilter = "all"
    private var currentSearchQuery = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_log_aktivitas, container, false)

        rvLogs = view.findViewById(R.id.rvLogs)
        etSearchLogs = view.findViewById(R.id.etSearchLogs)
        btnFilterLogs = view.findViewById(R.id.btnFilterLogs)

        rvLogs.layoutManager = LinearLayoutManager(context)

        // Ambil Data dari API
        loadLogsFromApi()

        logAdapter = LogAdapter(logList) { log ->
            showLogDetail(log)
        }
        rvLogs.adapter = logAdapter

        // Setup Filter
        btnFilterLogs.setOnClickListener { showFilterMenu(it) }

        // Setup Search
        etSearchLogs.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                currentSearchQuery = s.toString()
                applyFilterAndSearch()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        return view
    }

    private fun loadLogsFromApi() {
        val sharedPref = requireContext().getSharedPreferences("MonduSession", android.content.Context.MODE_PRIVATE)
        val idUser = sharedPref.getString("id_user", "") ?: ""
        val role = sharedPref.getString("role", "") ?: ""

        // Jika siswa, hanya ambil log miliknya. Jika admin, ambil semua.
        val url = if (role.lowercase() == "siswa") {
            "http://10.0.2.2/api_mondu/get_logs.php?id_user=$idUser"
        } else {
            "http://10.0.2.2/api_mondu/get_logs.php"
        }

        val request = StringRequest(Request.Method.GET, url,
            { response ->
                try {
                    val json = JSONObject(response)
                    if (json.getString("status") == "success") {
                        val data = json.getJSONArray("data")
                        fullLogList.clear()
                        for (i in 0 until data.length()) {
                            val obj = data.getJSONObject(i)
                            fullLogList.add(
                                LogActivity(
                                    obj.getString("id_log"),
                                    obj.getString("nama_user"),
                                    obj.getString("role"),
                                    obj.getString("aksi"),
                                    obj.getString("waktu")
                                )
                            )
                        }
                        applyFilterAndSearch()
                    }
                } catch (e: Exception) {
                    loadMockLogs()
                }
            },
            { loadMockLogs() }
        )
        Volley.newRequestQueue(requireContext()).add(request)
    }

    private fun loadMockLogs() {
        fullLogList.clear()
        fullLogList.add(LogActivity("1", "Sistem", "admin", "Belum ada aktivitas tercatat", "Baru saja"))
        
        logList.clear()
        logList.addAll(fullLogList)
        if (::logAdapter.isInitialized) {
            logAdapter.updateData(logList)
        }
    }

    private fun showFilterMenu(view: View) {
        val popup = androidx.appcompat.widget.PopupMenu(requireContext(), view)
        popup.menu.add("Semua")
        popup.menu.add("Guru")
        popup.menu.add("Siswa")
        popup.menu.add("Admin")

        popup.setOnMenuItemClickListener { item ->
            currentFilter = when (item.title) {
                "Guru" -> "guru"
                "Siswa" -> "siswa"
                "Admin" -> "admin"
                else -> "all"
            }
            applyFilterAndSearch()
            true
        }
        popup.show()
    }

    private fun applyFilterAndSearch() {
        logList.clear()
        
        val roleFiltered = if (currentFilter == "all") {
            fullLogList
        } else {
            fullLogList.filter { it.role.equals(currentFilter, ignoreCase = true) }
        }

        val finalFiltered = if (currentSearchQuery.isEmpty()) {
            roleFiltered
        } else {
            roleFiltered.filter { 
                it.aksi.contains(currentSearchQuery, ignoreCase = true) ||
                it.nama_user.contains(currentSearchQuery, ignoreCase = true)
            }
        }

        logList.addAll(finalFiltered)
        logAdapter.updateData(logList)
    }

    private fun showLogDetail(log: LogActivity) {
        val bottomSheet = com.google.android.material.bottomsheet.BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.layout_detail_log, null)

        val tvDetailAksi: android.widget.TextView = view.findViewById(R.id.tvDetailAksi)
        val tvDetailUser: android.widget.TextView = view.findViewById(R.id.tvDetailUser)
        val tvDetailWaktu: android.widget.TextView = view.findViewById(R.id.tvDetailWaktu)
        val btnTutupDetail: com.google.android.material.button.MaterialButton = view.findViewById(R.id.btnTutupDetail)

        tvDetailAksi.text = log.aksi
        tvDetailUser.text = "${log.nama_user} (${log.role})"
        tvDetailWaktu.text = "Hari ini, ${log.waktu}"

        btnTutupDetail.setOnClickListener {
            bottomSheet.dismiss()
        }

        bottomSheet.setContentView(view)
        bottomSheet.show()
    }
}