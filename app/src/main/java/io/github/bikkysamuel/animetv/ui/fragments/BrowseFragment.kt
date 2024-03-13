package io.github.bikkysamuel.animetv.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.bikkysamuel.animetv.R
import io.github.bikkysamuel.animetv.adapter.BrowseAnimeAdapter
import io.github.bikkysamuel.animetv.listeners.adapters.BrowseAnimeAdapterCallbackListener
import io.github.bikkysamuel.animetv.listeners.fragments.LoadDetailPageFragmentListener
import io.github.bikkysamuel.animetv.listeners.presenters.BrowseAnimePresenterCallbackListener
import io.github.bikkysamuel.animetv.models.BrowseAnimeItem
import io.github.bikkysamuel.animetv.presenters.BrowseFragmentPresenter
import io.github.bikkysamuel.animetv.utils.ShowToast

class BrowseFragment(private val loadDetailPageFragmentListener: LoadDetailPageFragmentListener)
    : Fragment(), BrowseAnimeAdapterCallbackListener, BrowseAnimePresenterCallbackListener {

    private var browseFragmentPresenter: BrowseFragmentPresenter? = null

    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnSub: Button
    private lateinit var btnDub: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root: View = inflater.inflate(R.layout.fragment_browse, container, false)

        initialiseViews(root)

        return root
    }

    private fun initialiseViews(root: View) {
        searchView = root.findViewById(R.id.searchView_browseAnime)
        recyclerView = root.findViewById(R.id.recView_browseAnime)
        btnSub = root.findViewById(R.id.btn_sub)
        btnDub = root.findViewById(R.id.btn_dub)

        // Avoid reload if navigated back to Browse Fragment
        if (recyclerView.adapter != null)
            return

        initializeBrowseAnimeList()
        addPaginationOnScroll()
        initializeSearchView()
        initializeButtonClicks()
    }

    private fun initializeBrowseAnimeList() {
        val browseAnimeItems: ArrayList<BrowseAnimeItem> =
            if (browseFragmentPresenter == null)
                ArrayList<BrowseAnimeItem>()
            else
                browseFragmentPresenter!!.getAnimeList()

        val gridLayoutManager: GridLayoutManager = GridLayoutManager(context, 2)
        recyclerView.layoutManager = gridLayoutManager

        val browseAnimeAdapter =
            BrowseAnimeAdapter(
                browseAnimeItems = browseAnimeItems,
                this
            )
        recyclerView.adapter = browseAnimeAdapter

        // Presenter should be last to load the data
        if (browseFragmentPresenter == null)
            browseFragmentPresenter = BrowseFragmentPresenter(browseAnimeItems = browseAnimeItems, this)
    }

    private fun addPaginationOnScroll() {
        this.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener () {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // Do nothing if recycler view adapter is null since data is not loaded.
                if (recyclerView.adapter == null)
                    return

                val gridLayoutManager: GridLayoutManager = recyclerView.layoutManager as GridLayoutManager
                val lastCompletelyVisibleItemPosition: Int = gridLayoutManager.findLastCompletelyVisibleItemPosition()
                val totalItems: Int = recyclerView.adapter!!.itemCount

                if (lastCompletelyVisibleItemPosition == totalItems - 1) {
                    ShowToast.showShortToastMsg("Loading....")
                    loadData(false)
                }
            }
        })
    }

    private fun initializeSearchView() {
        searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.apply {
                    loadData(true)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        searchView.setOnCloseListener {
            setButtonVisibilityOnSearchClose(true)
            false
        }
    }

    private fun setButtonVisibilityOnSearchClose(searchViewClosed: Boolean) {
        if (searchViewClosed) {
            btnSub.visibility = View.VISIBLE
            btnDub.visibility = View.VISIBLE
            btnSub.performClick()
        } else {
            btnSub.visibility = View.GONE
            btnDub.visibility = View.GONE
        }
    }

    private fun initializeButtonClicks() {
        btnSub.setOnClickListener {
            setButtonTextAndLoadData(dubVersionSelected = false)
        }
        btnDub.setOnClickListener {
            setButtonTextAndLoadData(dubVersionSelected = true)
        }
    }

    private fun setButtonTextAndLoadData(dubVersionSelected: Boolean) {
        "DUB${if (dubVersionSelected) " - Selected" else ""}".also { btnDub.text = it }
        "SUB${if (!dubVersionSelected) " - Selected" else ""}".also { btnSub.text = it }
        browseFragmentPresenter!!.showDubVersion(dubVersionSelected)
        loadData(resetData = true)
    }

    private fun loadData(resetData: Boolean) {
        val searchQuery: String = searchView.query.toString()
        if (searchQuery.isEmpty())
            browseFragmentPresenter!!.loadNextPage(resetData)
        else {
            browseFragmentPresenter!!.searchWithKeyword(searchQuery, resetData)
            setButtonVisibilityOnSearchClose(false)
        }
    }

    override fun updateListData(browseAnimeList: ArrayList<BrowseAnimeItem>, resetData: Boolean) {
        (recyclerView.adapter as BrowseAnimeAdapter).apply {
            if (resetData){
                notifyDataSetChanged()
                recyclerView.smoothScrollToPosition(0)
            }
            else
                notifyItemRangeChanged(itemCount - browseAnimeList.size, itemCount)
        }
    }

    override fun onAnimeItemClick(browseAnimeItem: BrowseAnimeItem) {
        loadDetailPageFragmentListener.loadVideoPlayerFragment(browseAnimeItem)
    }
}