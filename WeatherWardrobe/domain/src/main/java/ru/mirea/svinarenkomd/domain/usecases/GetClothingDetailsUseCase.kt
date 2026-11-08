package ru.mirea.svinarenkomd.domain.usecases

import ru.mirea.svinarenkomd.domain.models.ClothingItem
import ru.mirea.svinarenkomd.domain.repository.ClothingRepository

class GetClothingDetailsUseCase(
    private val clothingRepository: ClothingRepository
) {
    fun execute(id: Int): ClothingItem? {
        return clothingRepository.getClothingById(id)
    }
}
