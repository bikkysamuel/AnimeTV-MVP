package io.github.bikkysamuel.animetv.listeners

import io.github.bikkysamuel.animetv.models.BrowseAnimeItem

interface DatabaseHandlerListener {
    fun updateFavouriteInUI(currentFavouriteValue: Boolean)
    fun loadDashboardUI(animeList: List<BrowseAnimeItem>, resetData: Boolean)
}