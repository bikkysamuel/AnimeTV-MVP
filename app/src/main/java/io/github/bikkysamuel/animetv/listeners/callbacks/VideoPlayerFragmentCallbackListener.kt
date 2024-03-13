package io.github.bikkysamuel.animetv.listeners.callbacks

import java.io.Serializable

interface VideoPlayerFragmentCallbackListener : Serializable {
    fun goBackFromVideoPlayerPage()
    fun updateFavouritesInDashboard()
}