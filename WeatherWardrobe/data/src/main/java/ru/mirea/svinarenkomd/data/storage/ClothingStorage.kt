package ru.mirea.svinarenkomd.data.storage

import ru.mirea.svinarenkomd.data.storage.models.ClothingEntity

interface ClothingStorage {
    fun getAll(): List<ClothingEntity>
    fun save(entity: ClothingEntity): Boolean
    fun deleteById(id: Int): Boolean
}
