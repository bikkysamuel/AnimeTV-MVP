package io.github.bikkysamuel.animetv.ui.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.bikkysamuel.animetv.R
import io.github.bikkysamuel.animetv.adapter.VideoPlayerEpisodeListAdapter
import io.github.bikkysamuel.animetv.listeners.LoadSelectedVideoListener
import io.github.bikkysamuel.animetv.listeners.callbacks.VideoPlayerFragmentCallbackListener
import io.github.bikkysamuel.animetv.listeners.callbacks.VideoWebViewClientCallbackListener
import io.github.bikkysamuel.animetv.listeners.fragments.VideoPlayerFragmentListener
import io.github.bikkysamuel.animetv.models.BrowseAnimeItem
import io.github.bikkysamuel.animetv.models.VideoPlayerEpisodeItem
import io.github.bikkysamuel.animetv.presenters.VideoPlayerFragmentPresenter
import io.github.bikkysamuel.animetv.utils.Constants
import io.github.bikkysamuel.animetv.utils.MyUtils
import io.github.bikkysamuel.animetv.utils.VideoWebChromeClient
import io.github.bikkysamuel.animetv.utils.VideoWebViewClient


/**
 * A [Fragment] subclass.
 * Use the [VideoPlayerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VideoPlayerFragment : Fragment(), LoadSelectedVideoListener,
    VideoPlayerFragmentListener, VideoWebViewClientCallbackListener {

    private lateinit var browseAnimeItem: BrowseAnimeItem
    private lateinit var videoPlayerFragmentCallbackListener: VideoPlayerFragmentCallbackListener
    private lateinit var videoPlayerFragmentPresenter: VideoPlayerFragmentPresenter

    private lateinit var currentVideoFrame: String

    private lateinit var recyclerView: RecyclerView
    private lateinit var webViewVideoPlayer: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                browseAnimeItem = it.getSerializable(Constants.ANIME_LIST_ITEM, BrowseAnimeItem::class.java) as BrowseAnimeItem
                videoPlayerFragmentCallbackListener = it.getSerializable(Constants.GO_BACK_LISTENER, VideoPlayerFragmentCallbackListener::class.java) as VideoPlayerFragmentCallbackListener
            } else {
                browseAnimeItem = it.getSerializable(Constants.ANIME_LIST_ITEM) as BrowseAnimeItem
                videoPlayerFragmentCallbackListener = it.getSerializable(Constants.GO_BACK_LISTENER) as VideoPlayerFragmentCallbackListener
            }
        }

        initializeOnBackPressedCallback()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recView_videoPlayerWithEpisodeList)
        webViewVideoPlayer = view.findViewById(R.id.webView_videoPlayer)

        loadVideoPageData()
    }

    private fun initializeOnBackPressedCallback() {
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    isEnabled = false // This is required

                    if (isWebViewFullScreen()) {
                        collapseWebView()
                        webViewVideoPlayer.goBack()
                    } else {
                        // Handle the back button event
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                        videoPlayerFragmentCallbackListener.goBackFromVideoPlayerPage()
                    }
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
        // The callback can be enabled or disabled here or in handleOnBackPressed()
    }

    private fun loadVideoPageData() {
        videoPlayerFragmentPresenter = VideoPlayerFragmentPresenter(browseAnimeItem, this)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param browseAnimeItem Parameter 1.
         * @param videoPlayerFragmentCallbackListener Parameter 2.
         * @return A new instance of fragment DetailPageFragment.
         */
        @JvmStatic
        fun newInstance(browseAnimeItem: BrowseAnimeItem,
                        videoPlayerFragmentCallbackListener: VideoPlayerFragmentCallbackListener
        ) =
            VideoPlayerFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(Constants.ANIME_LIST_ITEM, browseAnimeItem)
                    putSerializable(Constants.GO_BACK_LISTENER, videoPlayerFragmentCallbackListener)
                }
            }
    }

    override fun loadSelectedVideoPage(videoPlayerEpisodeItem: VideoPlayerEpisodeItem) {
        val loadVideoUrl = videoPlayerEpisodeItem.videoUrl
        videoPlayerFragmentPresenter.getVideoDataFromUrl(videoUrl = loadVideoUrl)
    }

    override fun loadDataOnUi() {
        val linLayoutManager: LinearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linLayoutManager
        val videoPlayerEpisodeListAdapter: VideoPlayerEpisodeListAdapter =
            VideoPlayerEpisodeListAdapter(
                videoPlayerFragmentPresenter.getVideoPlayerPageData(),
                videoPlayerFragmentPresenter.getCurrentVideoTitle(),
                false, this, this)
        recyclerView.adapter = videoPlayerEpisodeListAdapter
        setWebViewVideoPlayer()

        browseAnimeItem.uploadedDate = videoPlayerFragmentPresenter.getLatestUpdatedOnDateTime()
        videoPlayerFragmentPresenter.updateFavourite(browseAnimeItem ,false)
    }

    override fun updateFavouriteItemBtnClicked() {
        browseAnimeItem.uploadedDate = videoPlayerFragmentPresenter.getLatestUpdatedOnDateTime()
        videoPlayerFragmentPresenter.updateFavourite(browseAnimeItem, true)
    }

    override fun updateFavouriteBtnValueInVideoPlayerPage(addAsFavourite: Boolean) {
        (recyclerView.adapter as VideoPlayerEpisodeListAdapter).updateFavouriteValue(addAsFavourite)
    }

    override fun updateFavouriteInUI(currentFavouriteValue: Boolean) {
        updateFavouriteBtnValueInVideoPlayerPage(currentFavouriteValue)
        videoPlayerFragmentCallbackListener.updateFavouritesInDashboard()
    }

    override fun loadDashboardUI(animeList: List<BrowseAnimeItem>, resetData: Boolean) {
        //Not required here
    }

    private fun animeCurrentVideoDataParser() {
        this.currentVideoFrame = videoPlayerFragmentPresenter.getVideoFrame()
    }

    private fun setWebViewVideoPlayer() {
        animeCurrentVideoDataParser()
        requireActivity().runOnUiThread {
            webViewVideoPlayer.webChromeClient = VideoWebChromeClient(requireActivity())
            webViewVideoPlayer.webViewClient = VideoWebViewClient(this)

            webViewVideoPlayer.settings.apply {
                userAgentString = Constants.USER_AGENT
                loadsImagesAutomatically = true
                javaScriptEnabled = true
                allowFileAccess = true
                javaScriptCanOpenWindowsAutomatically = true
                pluginState = WebSettings.PluginState.ON
                mediaPlaybackRequiresUserGesture = false
                domStorageEnabled = true
//                webViewVideoPlayer.settings.setAppCacheMaxSize(1024 * 8);
                setRenderPriority(WebSettings.RenderPriority.HIGH)
                cacheMode = WebSettings.LOAD_NO_CACHE
//                webViewVideoPlayer.settings.setAppCacheEnabled(true);
                cacheMode = WebSettings.LOAD_DEFAULT
            }

            webViewVideoPlayer.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
            webViewVideoPlayer.requestFocus(View.FOCUS_DOWN)
            webViewVideoPlayer.loadUrl("https:$currentVideoFrame")
        }
    }

    private fun isWebViewFullScreen(): Boolean {
        val layParams: ConstraintLayout.LayoutParams = webViewVideoPlayer.layoutParams as ConstraintLayout.LayoutParams
        return layParams.dimensionRatio == Constants.WEB_VIEW_FULLSCREEN_RATIO
    }

    override fun startAppChooserActivity(intentChooser: Intent) {
        startActivity(intentChooser)
    }

    override fun expandWebView() {
        MyUtils.setDimensionRatioOnWebViewVideoPlayer(webViewVideoPlayer, Constants.WEB_VIEW_FULLSCREEN_RATIO)
    }

    private fun collapseWebView() {
        MyUtils.setDimensionRatioOnWebViewVideoPlayer(webViewVideoPlayer, Constants.WEB_VIEW_NORMAL_WINDOW_RATIO)
    }
}