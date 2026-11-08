package ru.mirea.svinarenkomd.retrofitapp

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val imageViewAvatar: ImageView = itemView.findViewById(R.id.imageViewAvatar)
    val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
    val checkBoxCompleted: CheckBox = itemView.findViewById(R.id.checkBoxCompleted)
}
