

package com.example.ecomarket.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters // <-- ¡Importación necesaria!

// 1. Define las entidades y la versión
@Database(entities = [PurchaseEntity::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class) // <-- ¡CORRECCIÓN CLAVE! Le dice a Room cómo manejar java.util.Date
abstract class PurchaseDatabase : RoomDatabase() {

    // 2. Expone el DAO
    abstract fun purchaseDao(): PurchaseDao

    // 3. Companion Object para el Singleton Pattern
    companion object {
        @Volatile
        private var INSTANCE: PurchaseDatabase? = null

        fun getInstance(context: Context): PurchaseDatabase {
            // Usa el operador Elvis (?:) para asegurar que solo se crea una instancia.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PurchaseDatabase::class.java,
                    "purchase_database"
                )
                    .fallbackToDestructiveMigration() // Útil para desarrollo
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}