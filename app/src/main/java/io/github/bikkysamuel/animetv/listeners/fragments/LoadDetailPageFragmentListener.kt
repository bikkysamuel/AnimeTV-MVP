package io.github.bikkysamuel.animetv.listeners.fragments

import io.github.bikkysamuel.animetv.models.BrowseAnimeItem

interface LoadDetailPageFragmentListener {
    fun loadVideoPlayerFragment(browseAnimeItem: BrowseAnimeItem)
}