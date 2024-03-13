package io.github.bikkysamuel.animetv.listeners.adapters

import io.github.bikkysamuel.animetv.models.BrowseAnimeItem

interface BrowseAnimeAdapterCallbackListener {
    fun onAnimeItemClick(browseAnimeItem: BrowseAnimeItem)
}