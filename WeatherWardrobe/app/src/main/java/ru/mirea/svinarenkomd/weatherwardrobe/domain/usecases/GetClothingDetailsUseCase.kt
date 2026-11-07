package ru.mirea.svinarenkomd.weatherwardrobe.domain.usecases

import ru.mirea.svinarenkomd.weatherwardrobe.domain.models.ClothingItem
import ru.mirea.svinarenkomd.weatherwardrobe.domain.repository.ClothingRepository

class GetClothingDetailsUseCase(
    private val clothingRepository: ClothingRepository
) {
    fun execute(clothingId: Int): ClothingItem? {
        return clothingRepository.getClothingById(clothingId)
    }
}
