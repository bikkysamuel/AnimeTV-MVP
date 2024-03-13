package io.github.bikkysamuel.animetv.database

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.bikkysamuel.animetv.models.BrowseAnimeItem

@Database(entities = [BrowseAnimeItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun testAnimeDao(): TestAnimeDao
}