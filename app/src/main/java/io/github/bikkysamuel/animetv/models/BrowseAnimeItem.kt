package io.github.bikkysamuel.animetv.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class BrowseAnimeItem(
    val id: Int,
    @PrimaryKey var name: String,
    var description: String,
    var uploadedDate: String,
    var videoUrl: String,
    var imgUrl: String,
    var videoType: VideoType = VideoType.SUB
): Serializable