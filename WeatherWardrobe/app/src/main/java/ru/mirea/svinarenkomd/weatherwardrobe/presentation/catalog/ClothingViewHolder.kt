package ru.mirea.svinarenkomd.weatherwardrobe.presentation.catalog

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.mirea.svinarenkomd.domain.models.ClothingItem
import ru.mirea.svinarenkomd.weatherwardrobe.R

class ClothingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val imageViewClothing: ImageView = itemView.findViewById(R.id.imageViewClothing)
    val textViewClothingName: TextView = itemView.findViewById(R.id.textViewClothingName)
    val textViewCategoryAndTemp: TextView = itemView.findViewById(R.id.textViewCategoryAndTemp)
    val textViewClothingDescription: TextView = itemView.findViewById(R.id.textViewClothingDescription)
    val textViewSourceBadge: TextView = itemView.findViewById(R.id.textViewSourceBadge)

    fun bind(item: ClothingItem, onItemClick: (ClothingItem) -> Unit) {
        textViewClothingName.text = item.name
        textViewCategoryAndTemp.text = "${item.category} • ${item.minTemp}..${item.maxTemp}°C"
        textViewClothingDescription.text = item.description

        if (item.isFavorite) {
            textViewSourceBadge.text = "В гардеробе ★"
            textViewSourceBadge.setBackgroundColor(Color.parseColor("#DCFCE7"))
            textViewSourceBadge.setTextColor(Color.parseColor("#15803D"))
        } else {
            textViewSourceBadge.text = "Каталог"
            textViewSourceBadge.setBackgroundColor(Color.parseColor("#EEF2FF"))
            textViewSourceBadge.setTextColor(Color.parseColor("#4338CA"))
        }

        val iconRes = when {
            item.category.contains("Верхняя", ignoreCase = true) -> R.drawable.ic_clothing_outerwear
            item.category.contains("Низ", ignoreCase = true) -> R.drawable.ic_clothing_bottom
            item.category.contains("Обувь", ignoreCase = true) -> R.drawable.ic_clothing_shoes
            else -> R.drawable.ic_clothing_top
        }
        imageViewClothing.setImageResource(iconRes)

        itemView.setOnClickListener {
            onItemClick(item)
        }
    }
}
