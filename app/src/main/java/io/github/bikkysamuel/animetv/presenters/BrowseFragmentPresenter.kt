package io.github.bikkysamuel.animetv.presenters

import io.github.bikkysamuel.animetv.listeners.presenters.BrowseAnimePresenterCallbackListener
import io.github.bikkysamuel.animetv.models.BrowseAnimeItem
import io.github.bikkysamuel.animetv.network.AnimeApiHandler
import io.github.bikkysamuel.animetv.network.ApiResponseListener
import io.github.bikkysamuel.animetv.network.parsers.BrowseAnimeHomePageApiResponseParser
import io.github.bikkysamuel.animetv.utils.Constants
import io.github.bikkysamuel.animetv.utils.ShowToast

class BrowseFragmentPresenter(private val browseAnimeItems: ArrayList<BrowseAnimeItem>,
                              private val browseAnimePresenterCallbackListener: BrowseAnimePresenterCallbackListener)
    : ApiResponseListener<String> {

    private var browseAnimeHomePageApiResponseParser = BrowseAnimeHomePageApiResponseParser()

    private var currentHomePageNumber: Int = 0
    private var currentSearchPageNumber: Int = 0

    private var resetData: Boolean = false
    private var showDubVersions: Boolean = false

    fun getAnimeList() : ArrayList<BrowseAnimeItem> {
        return browseAnimeItems
    }

    init {
        loadHome()
    }

    fun showDubVersion(dubVersionSelected: Boolean) {
        showDubVersions = dubVersionSelected
    }

    private fun loadHome() {
        loadNextPage(true)
    }

    fun loadNextPage(resetData: Boolean) {
        currentSearchPageNumber = 0
        if (resetData)
            currentHomePageNumber = 0
        loadPage(++currentHomePageNumber)
    }

    private fun loadPage(pageNumber: Int) {
        clearDataIfPageNumberIsOne(pageNumber)
        AnimeApiHandler.getHomePageDataWithPageNumber(showDubVersions, pageNumber, this)
    }

    fun searchWithKeyword(keyword: String, resetData: Boolean) {
        currentHomePageNumber = 0
        if (resetData)
            currentSearchPageNumber = 0
        searchAnime(keyword, ++currentSearchPageNumber)
    }

    private fun searchAnime(keyword: String, pageNumber: Int) {
        clearDataIfPageNumberIsOne(pageNumber)
        AnimeApiHandler.searchWithKeyword(keyword, pageNumber, this)
    }

    private fun clearDataIfPageNumberIsOne(pageNumber: Int) {
        if (pageNumber == 1)
            resetData = true
    }

    override fun onApiResponseSuccess(responseAsHtml: String) {
        parseHtmlResponse(responseAsHtml = responseAsHtml)
    }

    override fun onApiResponseFailure(errorMessage: String) {
        ShowToast.showShortToastMsg(Constants.ERROR_MESSAGE_ON_API_FAILURE)
    }

    private fun parseHtmlResponse(responseAsHtml: String) {
        val newBrowseAnimeItems: ArrayList<BrowseAnimeItem> = browseAnimeHomePageApiResponseParser.parseResponseAsAnimeList(responseAsHtml = responseAsHtml)
        if (resetData) {
            browseAnimeItems.clear()
        }
        browseAnimeItems.addAll(newBrowseAnimeItems)
        browseAnimePresenterCallbackListener.updateListData(newBrowseAnimeItems, resetData = resetData)
        resetData = false
    }
}