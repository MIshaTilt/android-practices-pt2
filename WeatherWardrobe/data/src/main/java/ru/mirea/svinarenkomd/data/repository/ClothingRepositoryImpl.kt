package ru.mirea.svinarenkomd.data.repository

import ru.mirea.svinarenkomd.data.mappers.toDomain
import ru.mirea.svinarenkomd.data.mappers.toEntity
import ru.mirea.svinarenkomd.data.network.NetworkApi
import ru.mirea.svinarenkomd.data.storage.ClothingStorage
import ru.mirea.svinarenkomd.domain.models.ClothingItem
import ru.mirea.svinarenkomd.domain.repository.ClothingRepository

class ClothingRepositoryImpl(
    private val clothingStorage: ClothingStorage,
    private val networkApi: NetworkApi
) : ClothingRepository {

    override fun getCatalogForTemperature(temperature: Int): List<ClothingItem> {
        val savedIds = clothingStorage.getAll().map { it.id }.toSet()
        return networkApi.fetchClothingCatalog()
            .filter { temperature in it.minTemp..it.maxTemp }
            .map { dto -> dto.toDomain(isFavorite = dto.id in savedIds) }
    }

    override fun getClothingById(id: Int): ClothingItem? {
        val savedIds = clothingStorage.getAll().map { it.id }.toSet()
        val fromCatalog = networkApi.fetchClothingCatalog().find { it.id == id }
        if (fromCatalog != null) {
            return fromCatalog.toDomain(isFavorite = id in savedIds)
        }
        return clothingStorage.getAll().find { it.id == id }?.toDomain()
    }

    override fun getUserWardrobe(): List<ClothingItem> {
        return clothingStorage.getAll().map { it.toDomain() }
    }

    override fun saveToWardrobe(item: ClothingItem): Boolean {
        return clothingStorage.save(item.toEntity())
    }

    override fun removeFromWardrobe(itemId: Int): Boolean {
        return clothingStorage.deleteById(itemId)
    }
}
