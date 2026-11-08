package ru.mirea.svinarenkomd.data.storage.room

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.mirea.svinarenkomd.data.storage.models.ClothingEntity
import java.time.LocalDate

@Database(entities = [ClothingEntity::class], version = 1, exportSchema = false)
abstract class WardrobeDatabase : RoomDatabase() {

    abstract fun wardrobeDao(): WardrobeDao

    companion object {
        @Volatile
        private var daoInstance: WardrobeDao? = null

        fun getDao(context: Context): WardrobeDao {
            return daoInstance ?: synchronized(this) {
                val dao = try {
                    Room.databaseBuilder(
                        context.applicationContext,
                        WardrobeDatabase::class.java,
                        "wardrobe_db"
                    ).allowMainThreadQueries().build().wardrobeDao()
                } catch (_: Exception) {
                    SqliteBackedWardrobeDao(context.applicationContext)
                }
                daoInstance = dao
                dao
            }
        }
    }
}

private class SqliteBackedWardrobeDao(context: Context) :
    SQLiteOpenHelper(context, "wardrobe_db", null, 1), WardrobeDao {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS wardrobe_items (
                id INTEGER PRIMARY KEY NOT NULL,
                name TEXT NOT NULL,
                category TEXT NOT NULL,
                minTemp INTEGER NOT NULL,
                maxTemp INTEGER NOT NULL,
                imageUrl TEXT NOT NULL,
                description TEXT NOT NULL,
                savedDate TEXT NOT NULL
            )
            """.trimIndent()
        )
        // Начальные вещи в личном гардеробе по умолчанию
        val defaultDate = LocalDate.now().toString()
        insertDefault(
            db,
            ClothingEntity(
                id = 2,
                name = "Оверсайз худи с начёсом",
                category = "Верх (Худи / Толстовка)",
                minTemp = 8,
                maxTemp = 18,
                imageUrl = "https://images.unsplash.com/photo-1556821840-3a63f95609a7?w=600",
                description = "Мягкое хлопковое худи для прохладных дней.",
                savedDate = defaultDate
            )
        )
        insertDefault(
            db,
            ClothingEntity(
                id = 3,
                name = "Классические синие джинсы",
                category = "Низ (Джинсы / Брюки)",
                minTemp = -5,
                maxTemp = 22,
                imageUrl = "https://images.unsplash.com/photo-1542272604-787c3835535d?w=600",
                description = "Прямые джинсы из плотного денима.",
                savedDate = defaultDate
            )
        )
    }

    private fun insertDefault(db: SQLiteDatabase, item: ClothingEntity) {
        val values = ContentValues().apply {
            put("id", item.id)
            put("name", item.name)
            put("category", item.category)
            put("minTemp", item.minTemp)
            put("maxTemp", item.maxTemp)
            put("imageUrl", item.imageUrl)
            put("description", item.description)
            put("savedDate", item.savedDate)
        }
        db.insertWithOnConflict("wardrobe_items", null, values, SQLiteDatabase.CONFLICT_REPLACE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS wardrobe_items")
        onCreate(db)
    }

    override fun getAllItems(): List<ClothingEntity> {
        val list = mutableListOf<ClothingEntity>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT id, name, category, minTemp, maxTemp, imageUrl, description, savedDate FROM wardrobe_items ORDER BY id ASC", null)
        cursor.use { c ->
            while (c.moveToNext()) {
                list.add(
                    ClothingEntity(
                        id = c.getInt(0),
                        name = c.getString(1),
                        category = c.getString(2),
                        minTemp = c.getInt(3),
                        maxTemp = c.getInt(4),
                        imageUrl = c.getString(5),
                        description = c.getString(6),
                        savedDate = c.getString(7)
                    )
                )
            }
        }
        return list
    }

    override fun insertItem(item: ClothingEntity) {
        val db = writableDatabase
        insertDefault(db, item)
    }

    override fun deleteItemById(itemId: Int) {
        val db = writableDatabase
        db.delete("wardrobe_items", "id = ?", arrayOf(itemId.toString()))
    }
}
