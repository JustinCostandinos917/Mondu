package com.example.mondu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONException

class KelolaUserFragment : Fragment() {
    private lateinit var rvUsers: RecyclerView
    private lateinit var fabAddUser: FloatingActionButton
    private lateinit var userAdapter: UserAdapter
    private var userList = ArrayList<User>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_kelola_user, container, false)

        rvUsers = view.findViewById(R.id.rvUsers)
        fabAddUser = view.findViewById(R.id.fabAddUser)

        rvUsers.layoutManager = LinearLayoutManager(context)

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

        // Panggil fungsi untuk mengambil data dari SQL
        loadUsersFromDatabase()

        fabAddUser.setOnClickListener {
            val intent = Intent(context, AddUserActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun loadUsersFromDatabase() {
        // GANTI IP INI dengan IP Laptop kamu (kalau pakai localhost) atau URL Hosting kamu
        // Contoh kalau pakai emulator bawaan Android Studio ke localhost laptop: http://10.0.2.2/folder_kamu/get_users.php
        val url = "http://10.0.2.2/api_mondu/get_users.php"

        val queue = Volley.newRequestQueue(context)
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    userList.clear() // Bersihkan list lama
                    for (i in 0 until response.length()) {
                        val obj = response.getJSONObject(i)

                        // Bungkus data JSON dari PHP ke format objek Data Class Kotlin
                        val user = User(
                            obj.getString("id_user"),
                            obj.getString("username"),
                            obj.getString("email"),
                            obj.getString("nama_lengkap"),
                            obj.getString("role"),
                            if (obj.isNull("foto_profil")) null else obj.getString("foto_profil")
                        )
                        userList.add(user)
                    }
                    // Beritahu adapter kalau data SQL ter-update dan siap tampil
                    userAdapter.updateData(userList)

                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(context, "Gagal parsing data", Toast.LENGTH_SHORT).show()
                    Log.e("gnti", "Something went wrong: ${e.message}", e)
                }
            },
            { error ->
                Toast.makeText(context, "Error koneksi: ${error.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        )

        queue.add(jsonArrayRequest)
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
        loadUsersFromDatabase() // Otomatis refresh list data saat kembali dari halaman Add/Edit
    }
}