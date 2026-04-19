package com.example.vk.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        NameEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class MainDb: RoomDatabase() {
    abstract val dao: Dao

    companion object {
        fun createDataBase(context: Context): MainDb{
            return Room.databaseBuilder(
                context,
                MainDb::class.java,
                "test.db")
                    .fallbackToDestructiveMigration()
                    .build()
        }
    }
}