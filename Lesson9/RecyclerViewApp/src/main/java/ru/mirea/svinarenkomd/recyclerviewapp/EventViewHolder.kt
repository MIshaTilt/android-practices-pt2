package ru.mirea.svinarenkomd.recyclerviewapp

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val imageView: ImageView = itemView.findViewById(R.id.imageView)
    val eventNameView: TextView = itemView.findViewById(R.id.textViewEventName)
    val descriptionView: TextView = itemView.findViewById(R.id.textViewDescription)
}
