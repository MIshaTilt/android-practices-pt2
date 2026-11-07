package ru.mirea.svinarenkomd.weatherwardrobe.data.repository

import ru.mirea.svinarenkomd.weatherwardrobe.domain.models.ClothingItem
import ru.mirea.svinarenkomd.weatherwardrobe.domain.repository.ClothingRepository

class ClothingRepositoryImpl : ClothingRepository {

    private val mockCatalog = listOf(
        ClothingItem(
            id = 1,
            name = "Классический бежевый тренч",
            category = "Верхняя одежда",
            minTemp = 8,
            maxTemp = 18,
            imageUrl = "https://images.unsplash.com/photo-1591047139829-d91aecb6caea?w=600",
            description = "Непродуваемый плащ из плотного хлопка на осенне-весеннюю погоду."
        ),
        ClothingItem(
            id = 2,
            name = "Утепленное худи оверсайз",
            category = "Верх",
            minTemp = 10,
            maxTemp = 20,
            imageUrl = "https://images.unsplash.com/photo-1556821840-3a63f95609a7?w=600",
            description = "Мягкое хлопковое худи с начесом для прохладных дней."
        ),
        ClothingItem(
            id = 3,
            name = "Базовая белая футболка",
            category = "Верх",
            minTemp = 20,
            maxTemp = 35,
            imageUrl = "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=600",
            description = "Легкая дышащая футболка из 100% хлопка на теплую и жаркую погоду."
        ),
        ClothingItem(
            id = 4,
            name = "Зимний пуховик Аляска",
            category = "Верхняя одежда",
            minTemp = -25,
            maxTemp = 5,
            imageUrl = "https://images.unsplash.com/photo-1544923246-77307dd654cb?w=600",
            description = "Теплая зимняя куртка с капюшоном и защитой от ветра и снега."
        )
    )

    private val wardrobeItems = mutableListOf(
        mockCatalog[1].copy(isFavorite = true)
    )

    override fun getCatalog(): List<ClothingItem> = mockCatalog

    override fun getClothingById(clothingId: Int): ClothingItem? {
        return mockCatalog.find { it.id == clothingId }
            ?: wardrobeItems.find { it.id == clothingId }
    }

    override fun saveToWardrobe(item: ClothingItem): Boolean {
        wardrobeItems.removeAll { it.id == item.id && item.id != 0 }
        val newId = if (item.id == 0) (wardrobeItems.maxOfOrNull { it.id } ?: 10) + 1 else item.id
        wardrobeItems.add(item.copy(id = newId, isFavorite = true))
        return true
    }

    override fun getWardrobeItems(): List<ClothingItem> = wardrobeItems.toList()

    override fun removeFromWardrobe(clothingId: Int): Boolean {
        return wardrobeItems.removeAll { it.id == clothingId }
    }
}
