package io.github.bikkysamuel.animetv.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.bikkysamuel.animetv.models.BrowseAnimeItem

@Dao
interface TestAnimeDao {
    @Query("SELECT * FROM browseAnimeItem ORDER BY name ASC")
    suspend fun getAll(): List<BrowseAnimeItem>

    @Query("SELECT * FROM browseAnimeItem WHERE UPPER(name) LIKE (:name) ORDER BY name ASC")
    suspend fun findAllByNames(name: String): List<BrowseAnimeItem>

    @Query("SELECT * FROM browseAnimeItem WHERE name LIKE :name LIMIT 1")
    suspend fun findByName(name: String): BrowseAnimeItem

    @Insert
    suspend fun insertAll(vararg browseAnimeItem: BrowseAnimeItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(browseAnimeItem: BrowseAnimeItem)

    @Delete
    suspend fun delete(browseAnimeItem: BrowseAnimeItem)
}