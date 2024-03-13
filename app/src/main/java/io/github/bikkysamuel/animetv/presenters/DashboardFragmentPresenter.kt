package io.github.bikkysamuel.animetv.presenters

import io.github.bikkysamuel.animetv.database.DatabaseHandler
import io.github.bikkysamuel.animetv.listeners.fragments.DashboardFragmentListener
import io.github.bikkysamuel.animetv.listeners.presenters.DashboardPresenterCallbackListener
import io.github.bikkysamuel.animetv.models.BrowseAnimeItem

class DashboardFragmentPresenter(private val browseAnimeItems: ArrayList<BrowseAnimeItem>,
                                 private val dashboardPresenterCallbackListener: DashboardPresenterCallbackListener,
                                 private val dashboardFragmentListener: DashboardFragmentListener)
    : DatabaseHandler(dashboardFragmentListener){

    init {
        loadDashboardDataFromLocalStorage()
    }

    private fun clearData() {
        this.browseAnimeItems.clear()
    }

    fun getAnimeList() : ArrayList<BrowseAnimeItem> {
        return browseAnimeItems
    }

    fun updateAnimeList(animeList: List<BrowseAnimeItem>) {
        browseAnimeItems.addAll(animeList)
    }

    fun loadDashboardDataFromLocalStorage() {
        clearData()
        loadAllDataFromLocalStorage()
    }

    fun searchWithKeyword(searchKeyword: String, resetData: Boolean) {
        if (resetData)
            clearData()
        loadDataFromLocalStorage(searchKeyword)
    }
}