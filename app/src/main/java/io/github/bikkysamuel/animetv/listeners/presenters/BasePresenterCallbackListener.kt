package io.github.bikkysamuel.animetv.listeners.presenters

import io.github.bikkysamuel.animetv.models.BrowseAnimeItem

interface BasePresenterCallbackListener {
    fun updateListData(browseAnimeList: ArrayList<BrowseAnimeItem>, resetData: Boolean)
}