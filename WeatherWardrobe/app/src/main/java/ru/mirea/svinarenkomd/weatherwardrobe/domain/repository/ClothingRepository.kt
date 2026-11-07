package ru.mirea.svinarenkomd.weatherwardrobe.domain.repository

import ru.mirea.svinarenkomd.weatherwardrobe.domain.models.ClothingItem

interface ClothingRepository {
    fun getCatalog(): List<ClothingItem>
    fun getClothingById(clothingId: Int): ClothingItem?
    fun saveToWardrobe(item: ClothingItem): Boolean
    fun getWardrobeItems(): List<ClothingItem>
    fun removeFromWardrobe(clothingId: Int): Boolean
}
