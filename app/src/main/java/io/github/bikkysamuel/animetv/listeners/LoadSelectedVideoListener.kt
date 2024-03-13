package io.github.bikkysamuel.animetv.listeners

import io.github.bikkysamuel.animetv.models.VideoPlayerEpisodeItem

interface LoadSelectedVideoListener {
    fun loadSelectedVideoPage(videoPlayerEpisodeItem: VideoPlayerEpisodeItem)
}