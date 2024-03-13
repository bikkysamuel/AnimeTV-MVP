package io.github.bikkysamuel.animetv.network.parsers

import io.github.bikkysamuel.animetv.models.VideoPlayerEpisodeItem
import io.github.bikkysamuel.animetv.models.VideoPlayerPageData
import io.github.bikkysamuel.animetv.utils.HtmlDocumentCssQuery
import io.github.bikkysamuel.animetv.utils.MyUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class VideoPlayerPageApiResponseParser {

    private lateinit var htmlDocument: Document
    private lateinit var animeTitle: String
    private lateinit var animeDescription: String
    private lateinit var currentVideoTitle: String
    private var animeVideoFrameUrl: String = ""
    private lateinit var videoPlayerPageData: VideoPlayerPageData

    fun parseHtmlResponse(htmlStringResponse: String) {
        htmlDocument = Jsoup.parse(htmlStringResponse)
        detailPageParser()
    }

    fun getVideoPlayerPageData() : VideoPlayerPageData {
        return videoPlayerPageData
    }

    fun getVideoFrame(): String {
        return this.animeVideoFrameUrl
    }

    fun getCurrentVideoTitle(): String {
        return this.currentVideoTitle
    }

    fun getLatestEpisodeUploadDateTime(): String {
        return (this.videoPlayerPageData.videoPlayerEpisodeItems.first() as VideoPlayerEpisodeItem).uploadDate
    }

    private fun detailPageParser() {
        animeTitleParser()
        animeDescriptionParser()
        animeEpisodesListParser()
        animeCurrentVideoDataParser()
    }

    private fun animeTitleParser() {
        animeTitle = htmlDocument.select(HtmlDocumentCssQuery.VIDEO_PLAYER_ANIME_TITLE).text()
    }

    private fun animeDescriptionParser() {
        animeDescription = htmlDocument.select(HtmlDocumentCssQuery.VIDEO_PLAYER_ANIME_DESCRIPTION).text()
    }

    private fun animeCurrentVideoDataParser() {
        this.animeVideoFrameUrl = htmlDocument.select(HtmlDocumentCssQuery.VIDEO_FRAME).attr("src")
        this.currentVideoTitle = htmlDocument.select(HtmlDocumentCssQuery.VIDEO_PLAYER_CURRENT_EPISODE_TITLE).text()
    }

    private fun animeEpisodesListParser() {
        val elementsEpisodes = Elements()
        elementsEpisodes.addAll(htmlDocument.select(HtmlDocumentCssQuery.EPISODES))
        val videoPageEpisodeItems: ArrayList<VideoPlayerEpisodeItem> = ArrayList<VideoPlayerEpisodeItem>()
        for (element in elementsEpisodes) {
            val posterUrl = element.select(HtmlDocumentCssQuery.VIDEO_PLAYER_ANIME_POSTER_URL).attr("src")
            val videoUrl = element.attr("href")
            val videoType = element.select(HtmlDocumentCssQuery.VIDEO_PLAYER_ANIME_VIDEO_TYPE).text()
            val fullEpisodeName = element.select(HtmlDocumentCssQuery.VIDEO_PLAYER_ANIME_FULL_EPISODE_NAME).text()
            val uploadDate = element.select(HtmlDocumentCssQuery.VIDEO_PLAYER_ANIME_UPLOAD_DATE).text()
            val firstIndexOfEpisodeStr = fullEpisodeName.indexOf("Episode ")
            val lenOfEpisodeStr = "Episode ".length
            val spaceIndexAfterEpisodeNumber =
                fullEpisodeName.indexOf(" ", firstIndexOfEpisodeStr + lenOfEpisodeStr + 1)
            val episode = fullEpisodeName.substring(
                firstIndexOfEpisodeStr + lenOfEpisodeStr,
                if (spaceIndexAfterEpisodeNumber != -1) spaceIndexAfterEpisodeNumber else fullEpisodeName.length
            )

            val videoPlayerEpisodeItem = VideoPlayerEpisodeItem(name = animeTitle, imgUrl = posterUrl, videoUrl = videoUrl,
                videoType = MyUtils.parseVideoType(videoType), uploadDate = uploadDate, episode = episode, season = "")

            videoPageEpisodeItems.add(videoPlayerEpisodeItem)
        }
        videoPlayerPageData = VideoPlayerPageData(animeTitle, animeDescription, "", animeVideoFrameUrl,
            videoPlayerEpisodeItems = videoPageEpisodeItems)
    }
}