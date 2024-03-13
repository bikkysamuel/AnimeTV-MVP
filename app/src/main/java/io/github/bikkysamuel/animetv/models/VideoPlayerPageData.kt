package io.github.bikkysamuel.animetv.models

data class VideoPlayerPageData(
    val animeTitle: String,
    val animeDescription: String,
    val imgUrl: String,
    val videoUrl: String,
    val videoPlayerEpisodeItems: ArrayList<VideoPlayerEpisodeItem>
)
