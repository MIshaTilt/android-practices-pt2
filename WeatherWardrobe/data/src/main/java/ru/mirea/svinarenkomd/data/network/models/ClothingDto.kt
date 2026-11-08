package ru.mirea.svinarenkomd.data.network.models

data class ClothingDto(
    val id: Int,
    val name: String,
    val category: String,
    val minTemp: Int,
    val maxTemp: Int,
    val imageUrl: String,
    val description: String
)
