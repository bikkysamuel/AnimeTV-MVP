package io.github.bikkysamuel.animetv.models

import androidx.room.Entity

@Entity
data class VideoPlayerEpisodeItem(
    val name: String,
    val season: String,
    val episode: String,
    val imgUrl: String,
    val videoUrl: String,
    val videoType: VideoType,
    val uploadDate: String
)
