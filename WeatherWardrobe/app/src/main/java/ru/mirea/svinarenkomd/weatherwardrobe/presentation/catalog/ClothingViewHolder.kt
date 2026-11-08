package ru.mirea.svinarenkomd.weatherwardrobe.presentation.catalog

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.mirea.svinarenkomd.domain.models.ClothingItem
import ru.mirea.svinarenkomd.weatherwardrobe.R

class ClothingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val imageViewClothing: ImageView = itemView.findViewById(R.id.imageViewClothing)
    val textViewClothingName: TextView = itemView.findViewById(R.id.textViewClothingName)
    val textViewCategoryAndTemp: TextView = itemView.findViewById(R.id.textViewCategoryAndTemp)
    val textViewClothingDescription: TextView = itemView.findViewById(R.id.textViewClothingDescription)
    val textViewSourceBadge: TextView = itemView.findViewById(R.id.textViewSourceBadge)

    fun bind(item: ClothingItem, onItemClick: (ClothingItem) -> Unit) {
        val minSign = if (item.minTemp > 0) "+${item.minTemp}" else "${item.minTemp}"
        val maxSign = if (item.maxTemp > 0) "+${item.maxTemp}" else "${item.maxTemp}"

        textViewClothingName.text = item.name
        textViewCategoryAndTemp.text = "${item.category} · от ${minSign}°C до ${maxSign}°C"
        textViewClothingDescription.text = item.description

        if (item.isFavorite) {
            textViewSourceBadge.text = "В гардеробе"
            textViewSourceBadge.setBackgroundResource(R.drawable.bg_badge_saved)
            textViewSourceBadge.setTextColor(
                ContextCompat.getColor(itemView.context, R.color.text_success)
            )
        } else {
            textViewSourceBadge.text = "Подходит (${minSign}..${maxSign}°C)"
            textViewSourceBadge.setBackgroundResource(R.drawable.bg_badge_info)
            textViewSourceBadge.setTextColor(
                ContextCompat.getColor(itemView.context, R.color.text_info)
            )
        }

        val iconRes = when {
            item.category.contains("Верхняя", ignoreCase = true) -> R.drawable.ic_clothing_outerwear
            item.category.contains("Низ", ignoreCase = true) -> R.drawable.ic_clothing_bottom
            item.category.contains("Обувь", ignoreCase = true) -> R.drawable.ic_clothing_shoes
            else -> R.drawable.ic_clothing_top
        }

        if (item.imageUrl.isNotBlank()) {
            Picasso.get()
                .load(item.imageUrl)
                .placeholder(iconRes)
                .error(iconRes)
                .resize(200, 200)
                .centerCrop()
                .into(imageViewClothing)
        } else {
            imageViewClothing.setImageResource(iconRes)
        }

        itemView.setOnClickListener {
            onItemClick(item)
        }
    }
}
