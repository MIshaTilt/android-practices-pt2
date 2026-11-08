package ru.mirea.svinarenkomd.weatherwardrobe.presentation.catalog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.mirea.svinarenkomd.domain.models.ClothingItem
import ru.mirea.svinarenkomd.weatherwardrobe.R

class ClothingAdapter(
    private val onItemClick: (ClothingItem) -> Unit = {}
) : RecyclerView.Adapter<ClothingViewHolder>() {

    private val items = mutableListOf<ClothingItem>()

    fun setItems(newItems: List<ClothingItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClothingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_clothing, parent, false)
        return ClothingViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClothingViewHolder, position: Int) {
        holder.bind(items[position], onItemClick)
    }

    override fun getItemCount(): Int = items.size
}
