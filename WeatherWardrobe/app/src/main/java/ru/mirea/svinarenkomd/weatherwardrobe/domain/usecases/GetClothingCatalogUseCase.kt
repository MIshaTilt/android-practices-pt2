package ru.mirea.svinarenkomd.weatherwardrobe.domain.usecases

import ru.mirea.svinarenkomd.weatherwardrobe.domain.models.ClothingItem
import ru.mirea.svinarenkomd.weatherwardrobe.domain.repository.ClothingRepository

class GetClothingCatalogUseCase(
    private val clothingRepository: ClothingRepository
) {
    fun execute(currentTemp: Int? = null): List<ClothingItem> {
        val allItems = clothingRepository.getCatalog()
        return if (currentTemp != null) {
            allItems.filter { currentTemp in it.minTemp..it.maxTemp }
        } else {
            allItems
        }
    }
}
