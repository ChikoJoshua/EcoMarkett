// Archivo: com/example/ecomarket/data/db/DateConverter.kt

package com.example.ecomarket.data.db

import androidx.room.TypeConverter
import java.util.Date

/**
 * Convierte objetos java.util.Date a Long (timestamp) para que Room pueda almacenarlos
 * y viceversa.
 */
class DateConverter {

    // Convierte Long (almacenado en DB) a Date (usado en Kotlin)
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    // Convierte Date (usado en Kotlin) a Long (almacenado en DB)
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}