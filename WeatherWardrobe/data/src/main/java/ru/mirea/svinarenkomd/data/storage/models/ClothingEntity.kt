package ru.mirea.svinarenkomd.data.storage.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wardrobe_items")
data class ClothingEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val category: String,
    val minTemp: Int,
    val maxTemp: Int,
    val imageUrl: String,
    val description: String,
    val savedDate: String
)
