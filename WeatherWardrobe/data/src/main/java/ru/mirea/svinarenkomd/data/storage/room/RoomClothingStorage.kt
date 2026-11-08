package ru.mirea.svinarenkomd.data.storage.room

import android.content.Context
import ru.mirea.svinarenkomd.data.storage.ClothingStorage
import ru.mirea.svinarenkomd.data.storage.models.ClothingEntity

class RoomClothingStorage(context: Context) : ClothingStorage {

    private val wardrobeDao: WardrobeDao = WardrobeDatabase.getDao(context)

    override fun getAll(): List<ClothingEntity> {
        return wardrobeDao.getAllItems()
    }

    override fun save(entity: ClothingEntity): Boolean {
        wardrobeDao.insertItem(entity)
        return true
    }

    override fun deleteById(id: Int): Boolean {
        wardrobeDao.deleteItemById(id)
        return true
    }
}
