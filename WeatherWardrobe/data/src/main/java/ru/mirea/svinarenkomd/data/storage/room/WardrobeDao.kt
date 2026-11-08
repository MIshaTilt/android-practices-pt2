package ru.mirea.svinarenkomd.data.storage.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.mirea.svinarenkomd.data.storage.models.ClothingEntity

@Dao
interface WardrobeDao {
    @Query("SELECT * FROM wardrobe_items")
    fun getAllItems(): List<ClothingEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(item: ClothingEntity)

    @Query("DELETE FROM wardrobe_items WHERE id = :itemId")
    fun deleteItemById(itemId: Int)
}
