package io.github.bikkysamuel.animetv.database

import io.github.bikkysamuel.animetv.app.MyApp
import io.github.bikkysamuel.animetv.listeners.DatabaseHandlerListener
import io.github.bikkysamuel.animetv.models.BrowseAnimeItem
import io.github.bikkysamuel.animetv.utils.MyUtils

open class DatabaseHandler(private val databaseHandlerListener: DatabaseHandlerListener) {

    private fun checkIfFavouriteInDb(animeEpisodeItem: BrowseAnimeItem, updateDbOnResponse: Boolean) {
        MyUtils.runSuspendFunction {
            animeEpisodeItem.name = MyUtils.getAnimeTitle(animeEpisodeItem.name)
            val animeList: List<BrowseAnimeItem> = MyApp.roomDb.testAnimeDao().findAllByNames(
                animeEpisodeItem.name.uppercase()
            )
            var isFavouriteFromDb: Boolean = false
            for (anime in animeList) {

                if (anime.name == animeEpisodeItem.name) {
                    isFavouriteFromDb = true
                    break
                }
            }

            if (updateDbOnResponse) {
                updateFavouriteInDb(animeEpisodeItem, !isFavouriteFromDb)
            } else if (isFavouriteFromDb) { // Update anime data in DB
                updateFavouriteInDb(animeEpisodeItem, true)
            }

            if (updateDbOnResponse xor isFavouriteFromDb)
                databaseHandlerListener.updateFavouriteInUI(true)
            else
                databaseHandlerListener.updateFavouriteInUI(false)
        }
    }

    private suspend fun updateFavouriteInDb(animeEpisodeItem: BrowseAnimeItem, addAsFavourite: Boolean) {
        animeEpisodeItem.name = MyUtils.getAnimeTitle(animeEpisodeItem.name)
        if (addAsFavourite) {
            addToFavourite(animeEpisodeItem)
        } else {
            removeFromFavourite(animeEpisodeItem)
        }
    }

    private suspend fun addToFavourite(browseAnimeItem: BrowseAnimeItem) {
        MyApp.roomDb.testAnimeDao().insert(browseAnimeItem)
    }

    private suspend fun removeFromFavourite(browseAnimeItem: BrowseAnimeItem) {
        MyApp.roomDb.testAnimeDao().delete(browseAnimeItem)
    }

    fun updateFavourite(animeEpisodeItem: BrowseAnimeItem, updateDataInDb: Boolean) {
        checkIfFavouriteInDb(animeEpisodeItem, updateDbOnResponse = updateDataInDb)
    }

    fun loadAllDataFromLocalStorage() {
        MyUtils.runSuspendFunction {
            val animeList = MyApp.roomDb.testAnimeDao().getAll()
            databaseHandlerListener.loadDashboardUI(animeList, resetData = true)
        }
    }

    fun loadDataFromLocalStorage(searchKeyword: String) {
        MyUtils.runSuspendFunction {
            val regexSearchKeyword = "%${searchKeyword.uppercase()}%"
            val animeList = MyApp.roomDb.testAnimeDao().findAllByNames(name = regexSearchKeyword)
            databaseHandlerListener.loadDashboardUI(animeList, resetData = true)
        }
    }
}