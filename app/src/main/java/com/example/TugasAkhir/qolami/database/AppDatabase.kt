package com.example.TugasAkhir.qolami.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.TugasAkhir.qolami.data.Drawing
import com.example.TugasAkhir.qolami.data.TestResult

@Database(entities = [Drawing::class, TestResult::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun drawingDao(): DrawingDao
    abstract fun testResultDao(): TestResultDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "drawing_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}