package io.github.bikkysamuel.animetv.presenters

import io.github.bikkysamuel.animetv.database.DatabaseHandler
import io.github.bikkysamuel.animetv.listeners.fragments.VideoPlayerFragmentListener
import io.github.bikkysamuel.animetv.models.BrowseAnimeItem
import io.github.bikkysamuel.animetv.models.VideoPlayerPageData
import io.github.bikkysamuel.animetv.network.AnimeApiHandler
import io.github.bikkysamuel.animetv.network.ApiResponseListener
import io.github.bikkysamuel.animetv.network.parsers.VideoPlayerPageApiResponseParser
import io.github.bikkysamuel.animetv.utils.Constants
import io.github.bikkysamuel.animetv.utils.MyLogger
import io.github.bikkysamuel.animetv.utils.ShowToast

class VideoPlayerFragmentPresenter(private val browseAnimeItem: BrowseAnimeItem, private val videoPlayerFragmentListener: VideoPlayerFragmentListener)
    : ApiResponseListener<String>, DatabaseHandler(videoPlayerFragmentListener) {

    private var videoPlayerPageApiResponseParser = VideoPlayerPageApiResponseParser()

    init {
        getVideoData()
    }

    private fun getVideoData() {
        getVideoDataFromUrl(browseAnimeItem.videoUrl)
    }

    fun getVideoDataFromUrl(videoUrl: String) {
        AnimeApiHandler.getVideoPlayerData(videoUrl, this)
    }

    override fun onApiResponseSuccess(responseAsHtml: String) {
        videoPlayerPageApiResponseParser.parseHtmlResponse(responseAsHtml)
        videoPlayerFragmentListener.loadDataOnUi()
    }

    override fun onApiResponseFailure(errorMessage: String) {
        MyLogger.d(this, errorMessage)
        ShowToast.showShortToastMsg(Constants.ERROR_MESSAGE_ON_API_FAILURE)
    }

    fun getVideoFrame() : String {
        return videoPlayerPageApiResponseParser.getVideoFrame()
    }

    fun getCurrentVideoTitle(): String {
        return videoPlayerPageApiResponseParser.getCurrentVideoTitle()
    }

    fun getVideoPlayerPageData() : VideoPlayerPageData {
        return videoPlayerPageApiResponseParser.getVideoPlayerPageData()
    }

    fun getLatestUpdatedOnDateTime() : String {
        return videoPlayerPageApiResponseParser.getLatestEpisodeUploadDateTime()
    }
}