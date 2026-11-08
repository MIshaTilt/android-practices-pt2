package ru.mirea.svinarenkomd.domain.usecases

import ru.mirea.svinarenkomd.domain.repository.ClothingRepository

class RemoveClothingFromWardrobeUseCase(
    private val clothingRepository: ClothingRepository
) {
    fun execute(itemId: Int): Boolean {
        return clothingRepository.removeFromWardrobe(itemId)
    }
}
