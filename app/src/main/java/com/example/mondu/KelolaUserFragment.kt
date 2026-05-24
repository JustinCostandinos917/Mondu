package com.example.mondu

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONException

class KelolaUserFragment : Fragment() {
    private lateinit var rvUsers: RecyclerView
    private lateinit var fabAddUser: FloatingActionButton
    private lateinit var btnFilterUsers: android.widget.ImageButton
    private lateinit var etSearchUsers: TextInputEditText
    private lateinit var userAdapter: UserAdapter
    private var userList = ArrayList<User>()
    private var fullUserList = ArrayList<User>()
    private var currentFilter = "all"
    private var currentSearchQuery = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_kelola_user, container, false)

        rvUsers = view.findViewById(R.id.rvUsers)
        fabAddUser = view.findViewById(R.id.fabAddUser)
        btnFilterUsers = view.findViewById(R.id.btnFilterUsers)
        etSearchUsers = view.findViewById(R.id.etSearchUsers)

        rvUsers.layoutManager = LinearLayoutManager(context)

        btnFilterUsers.setOnClickListener { showFilterMenu(it) }

        etSearchUsers.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                currentSearchQuery = s.toString()
                applyFilterAndSearch()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Inisialisasi adapter dengan list kosong terlebih dahulu
        userAdapter = UserAdapter(
            userList,
            onEditClick = { user ->
                val intent = Intent(context, EditUserActivity::class.java).apply {
                    putExtra("id_user", user.id_user)
                    putExtra("username", user.username)
                    putExtra("nama_lengkap", user.nama_lengkap)
                    putExtra("email", user.email)
                    putExtra("role", user.role)
                }
                startActivity(intent)
            },
            onDeleteClick = { user ->
                tampilkanDialogHapus(user)
            }
        )
        rvUsers.adapter = userAdapter

        loadUsersFromDatabase()

        fabAddUser.setOnClickListener {
            val intent = Intent(context, AddUserActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun showFilterMenu(view: View) {
        val popup = androidx.appcompat.widget.PopupMenu(requireContext(), view)
        popup.menu.add("Semua")
        popup.menu.add("Guru")
        popup.menu.add("Siswa")
        popup.menu.add("Admin")

        popup.setOnMenuItemClickListener { item ->
            val selectedRole = when (item.title) {
                "Guru" -> "guru"
                "Siswa" -> "siswa"
                "Admin" -> "admin"
                else -> "all"
            }
            applyFilter(selectedRole)
            true
        }
        popup.show()
    }

    private fun loadUsersFromDatabase() {
        val url = "http://10.0.2.2/api_mondu/get_users.php"

        val queue = Volley.newRequestQueue(context)
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    fullUserList.clear() // Bersihkan list master
                    for (i in 0 until response.length()) {
                        val obj = response.getJSONObject(i)

                        val user = User(
                            obj.getString("id_user"),
                            obj.getString("username"),
                            obj.getString("email"),
                            obj.getString("nama_lengkap"),
                            obj.getString("role"),
                            if (obj.isNull("foto_profil")) null else obj.getString("foto_profil")
                        )
                        fullUserList.add(user)
                    }
                    

                    applyFilterAndSearch()

                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(context, "Gagal parsing data", Toast.LENGTH_SHORT).show()
                    Log.e("KelolaUserFragment", "Something went wrong: ${e.message}", e)
                }
            },
            { error ->
                Toast.makeText(context, "Error koneksi: ${error.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        )

        queue.add(jsonArrayRequest)
    }

    private fun applyFilter(role: String) {
        currentFilter = role
        applyFilterAndSearch()
    }

    private fun applyFilterAndSearch() {
        userList.clear()
        
        // Buat filter
        val roleFiltered = if (currentFilter == "all") {
            fullUserList
        } else {
            fullUserList.filter { it.role.equals(currentFilter, ignoreCase = true) }
        }


        val finalFiltered = if (currentSearchQuery.isEmpty()) {
            roleFiltered
        } else {
            roleFiltered.filter { 
                it.nama_lengkap.contains(currentSearchQuery, ignoreCase = true) ||
                it.username.contains(currentSearchQuery, ignoreCase = true)
            }
        }

        userList.addAll(finalFiltered)
        userAdapter.updateData(userList)
    }

    private fun tampilkanDialogHapus(user: User) {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Hapus User")
            .setMessage("Apakah Anda yakin ingin menghapus ${user.nama_lengkap}?")
            .setPositiveButton("Hapus") { _, _ ->
                eksekusiHapusUser(user.id_user)
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun eksekusiHapusUser(idUser: String) {
        val url = "http://10.0.2.2/api_mondu/delete_user.php"
        val queue = Volley.newRequestQueue(context)

        val stringRequest = object : com.android.volley.toolbox.StringRequest(
            Request.Method.POST, url,
            { response ->
                try {
                    val res = org.json.JSONObject(response)
                    Toast.makeText(context, res.getString("message"), Toast.LENGTH_SHORT).show()
                    if (res.getString("status") == "success") {
                        loadUsersFromDatabase() // Refresh list setelah berhasil menghapus
                    }
                } catch (e: Exception) { e.printStackTrace() }
            },
            { error -> Toast.makeText(context, "Gagal koneksi server", Toast.LENGTH_SHORT).show() }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["id_user"] = idUser
                return params
            }
        }
        queue.add(stringRequest)
    }

    override fun onResume() {
        super.onResume()
        loadUsersFromDatabase()
    }
}
