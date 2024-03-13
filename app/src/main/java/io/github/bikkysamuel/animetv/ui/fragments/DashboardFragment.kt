package io.github.bikkysamuel.animetv.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.bikkysamuel.animetv.R
import io.github.bikkysamuel.animetv.adapter.BrowseAnimeAdapter
import io.github.bikkysamuel.animetv.listeners.adapters.BrowseAnimeAdapterCallbackListener
import io.github.bikkysamuel.animetv.listeners.fragments.DashboardFragmentListener
import io.github.bikkysamuel.animetv.listeners.fragments.LoadDetailPageFragmentListener
import io.github.bikkysamuel.animetv.listeners.presenters.DashboardPresenterCallbackListener
import io.github.bikkysamuel.animetv.models.BrowseAnimeItem
import io.github.bikkysamuel.animetv.presenters.DashboardFragmentPresenter

class DashboardFragment(private val loadDetailPageFragmentListener: LoadDetailPageFragmentListener)
    : Fragment(), DashboardPresenterCallbackListener, DashboardFragmentListener, BrowseAnimeAdapterCallbackListener {

    private var dashboardFragmentPresenter: DashboardFragmentPresenter? = null

    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root:View = inflater.inflate(R.layout.fragment_dashboard, container, false)

        initialiseViews(root)

        return root
    }

    private fun initialiseViews(root: View) {
        searchView = root.findViewById(R.id.searchView_dashboardAnime)
        recyclerView = root.findViewById(R.id.recView_dashboardAnime)

        if (recyclerView.adapter != null)
            return

        initializeDashboardAnimeList()
        initializeSearchView()
    }

    private fun initializeDashboardAnimeList() {
        val browseAnimeItems: ArrayList<BrowseAnimeItem> =
            if (dashboardFragmentPresenter == null)
                ArrayList<BrowseAnimeItem>()
            else
                dashboardFragmentPresenter!!.getAnimeList()

        val gridLayoutManager: GridLayoutManager = GridLayoutManager(context, 2)
        recyclerView.layoutManager = gridLayoutManager

        val browseAnimeAdapter =
            BrowseAnimeAdapter(
                browseAnimeItems = browseAnimeItems,
                this
            )
        recyclerView.adapter = browseAnimeAdapter

        // This should be last to load the data
        if (dashboardFragmentPresenter == null)
            dashboardFragmentPresenter = DashboardFragmentPresenter(browseAnimeItems = browseAnimeItems,
                this, this)
    }

    private fun initializeSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.apply {
                    loadData()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.apply {
                    loadData()
                }
                return false
            }
        })
        searchView.setOnCloseListener {
            loadData()
            false
        }
    }

    private fun loadData() {
        val searchQuery: String = searchView.query.toString()
        if (searchQuery.isEmpty())
            getDataFromLocalStorage()
        else
            searchDataInLocalStorage(searchQuery)
    }

    private fun getDataFromLocalStorage() {
        this.dashboardFragmentPresenter!!.loadDashboardDataFromLocalStorage()
    }

    private fun searchDataInLocalStorage(searchQuery: String) {
        dashboardFragmentPresenter!!.searchWithKeyword(searchQuery, true)
    }

    fun updateFavouriteList() {
        dashboardFragmentPresenter = null
        initializeDashboardAnimeList()
    }

    override fun updateListData(browseAnimeList: ArrayList<BrowseAnimeItem>, resetData: Boolean) {
        (recyclerView.adapter as BrowseAnimeAdapter).apply {
            if (resetData){
                dashboardFragmentPresenter!!.updateAnimeList(animeList = browseAnimeList)
                recyclerView.smoothScrollToPosition(0)
                notifyDataSetChanged()
            }
            else
                notifyItemRangeChanged(itemCount - browseAnimeList.size, itemCount)
        }
    }

    override fun onAnimeItemClick(browseAnimeItem: BrowseAnimeItem) {
        loadDetailPageFragmentListener.loadVideoPlayerFragment(browseAnimeItem)
    }

    override fun updateFavouriteInUI(currentFavouriteValue: Boolean) {
        //Not required for dashboard
    }

    override fun loadDashboardUI(animeList: List<BrowseAnimeItem>, resetData: Boolean) {
        updateListData(animeList as ArrayList<BrowseAnimeItem>, resetData)
    }
}