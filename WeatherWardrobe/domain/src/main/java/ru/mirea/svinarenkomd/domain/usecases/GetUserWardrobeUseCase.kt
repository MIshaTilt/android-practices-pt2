package ru.mirea.svinarenkomd.domain.usecases

import ru.mirea.svinarenkomd.domain.models.ClothingItem
import ru.mirea.svinarenkomd.domain.repository.ClothingRepository

class GetUserWardrobeUseCase(
    private val clothingRepository: ClothingRepository
) {
    fun execute(): List<ClothingItem> {
        return clothingRepository.getUserWardrobe()
    }
}
