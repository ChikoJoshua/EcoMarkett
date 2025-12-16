

package com.example.ecomarket.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters // <-- ¡Importación necesaria!

@Database(entities = [PurchaseEntity::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class PurchaseDatabase : RoomDatabase() {

    abstract fun purchaseDao(): PurchaseDao

    companion object {
        @Volatile
        private var INSTANCE: PurchaseDatabase? = null

        fun getInstance(context: Context): PurchaseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PurchaseDatabase::class.java,
                    "purchase_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}