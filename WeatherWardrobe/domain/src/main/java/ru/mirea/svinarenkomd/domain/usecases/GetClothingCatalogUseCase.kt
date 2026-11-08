package ru.mirea.svinarenkomd.domain.usecases

import ru.mirea.svinarenkomd.domain.models.ClothingItem
import ru.mirea.svinarenkomd.domain.repository.ClothingRepository

class GetClothingCatalogUseCase(
    private val clothingRepository: ClothingRepository
) {
    fun execute(temperature: Int): List<ClothingItem> {
        return clothingRepository.getCatalogForTemperature(temperature)
    }
}
