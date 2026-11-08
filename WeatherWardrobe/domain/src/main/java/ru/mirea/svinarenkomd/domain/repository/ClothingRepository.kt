package ru.mirea.svinarenkomd.domain.repository

import ru.mirea.svinarenkomd.domain.models.ClothingItem

interface ClothingRepository {
    fun getCatalogForTemperature(temperature: Int): List<ClothingItem>
    fun getClothingById(id: Int): ClothingItem?
    fun getUserWardrobe(): List<ClothingItem>
    fun saveToWardrobe(item: ClothingItem): Boolean
    fun removeFromWardrobe(itemId: Int): Boolean
}
