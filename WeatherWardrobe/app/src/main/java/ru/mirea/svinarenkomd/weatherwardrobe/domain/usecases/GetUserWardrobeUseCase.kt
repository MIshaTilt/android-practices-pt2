package ru.mirea.svinarenkomd.weatherwardrobe.domain.usecases

import ru.mirea.svinarenkomd.weatherwardrobe.domain.models.ClothingItem
import ru.mirea.svinarenkomd.weatherwardrobe.domain.repository.ClothingRepository

class GetUserWardrobeUseCase(
    private val clothingRepository: ClothingRepository
) {
    fun execute(): List<ClothingItem> {
        return clothingRepository.getWardrobeItems()
    }
}
