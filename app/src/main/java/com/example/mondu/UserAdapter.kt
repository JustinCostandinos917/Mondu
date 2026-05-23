package com.example.mondu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView

class UserAdapter(
    private var userList: List<User>,
    private val onEditClick: (User) -> Unit,
    private val onDeleteClick: (User) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivUserRow: ShapeableImageView = itemView.findViewById(R.id.ivUserRow)
        val tvNamaUserRow: TextView = itemView.findViewById(R.id.tvNamaUserRow)
        val tvEmailRoleRow: TextView = itemView.findViewById(R.id.tvEmailRoleRow)
        val btnMenuMore: ImageButton = itemView.findViewById(R.id.btnMenuMore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.tvNamaUserRow.text = user.nama_lengkap
        holder.tvEmailRoleRow.text = "${user.email} • ${user.role}"

        // Set action popup menu untuk Edit & Delete saat tombol 3 titik diklik
        holder.btnMenuMore.setOnClickListener { view ->
            val popup = PopupMenu(view.context, view)
            popup.menu.add("Edit")
            popup.menu.add("Hapus")

            popup.setOnMenuItemClickListener { item ->
                when (item.title) {
                    "Edit" -> onEditClick(user)
                    "Hapus" -> onDeleteClick(user)
                }
                true
            }
            popup.show()
        }
    }

    override fun getItemCount(): Int = userList.size

    // Fungsi untuk memperbarui data di list saat ada perubahan (CRUD)
    fun updateData(newList: List<User>) {
        this.userList = newList
        notifyDataSetChanged()
    }
}