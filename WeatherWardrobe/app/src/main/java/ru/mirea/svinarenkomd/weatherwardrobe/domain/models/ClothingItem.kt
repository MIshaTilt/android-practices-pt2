package ru.mirea.svinarenkomd.weatherwardrobe.domain.models

data class ClothingItem(
    val id: Int,
    val name: String,
    val category: String,
    val minTemp: Int,
    val maxTemp: Int,
    val imageUrl: String,
    val description: String,
    val isFavorite: Boolean = false
)
