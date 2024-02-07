package com.imagegallery.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ImageEntity::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {

    abstract fun imageDao(): ImageDao

    companion object {
        private lateinit var database: AppDataBase

        fun buildDataBase(context: Context): AppDataBase {
            if (!this::database.isInitialized) {
                synchronized(this) {
                    database = Room.databaseBuilder(
                        context,
                        AppDataBase::class.java, context.packageName.toString()
                    ).build()
                }
            }
            return database
        }
    }
}