package io.github.bikkysamuel.animetv.listeners.fragments

import io.github.bikkysamuel.animetv.listeners.DatabaseHandlerListener

interface VideoPlayerFragmentListener : DatabaseHandlerListener {
    fun loadDataOnUi()
    fun updateFavouriteItemBtnClicked()
    fun updateFavouriteBtnValueInVideoPlayerPage(addAsFavourite: Boolean)
}