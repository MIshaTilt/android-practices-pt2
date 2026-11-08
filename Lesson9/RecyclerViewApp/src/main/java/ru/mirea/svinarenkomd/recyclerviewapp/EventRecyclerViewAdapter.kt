package ru.mirea.svinarenkomd.recyclerviewapp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class EventRecyclerViewAdapter(
    private val events: List<HistoricalEvent>
) : RecyclerView.Adapter<EventViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        context = parent.context
        val recyclerViewItem = LayoutInflater.from(context)
            .inflate(R.layout.event_item_view, parent, false)
        return EventViewHolder(recyclerViewItem)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        val pkgName = context.packageName
        val resId = context.resources.getIdentifier(event.imageResName, "drawable", pkgName)
        if (resId != 0) {
            holder.imageView.setImageResource(resId)
        }
        holder.eventNameView.text = event.eventName
        holder.descriptionView.text = event.description

        holder.itemView.setOnClickListener {
            Toast.makeText(
                context,
                "Выбрано событие: ${event.eventName}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun getItemCount(): Int = events.size
}
