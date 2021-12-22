package com.example.part03_ch04_bookreview

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.part03_ch04_bookreview.dao.HistoryDao
import com.example.part03_ch04_bookreview.model.History

@Database(entities = [History::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao

}