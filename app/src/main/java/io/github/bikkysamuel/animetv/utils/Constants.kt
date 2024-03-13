package io.github.bikkysamuel.animetv.utils

object Constants {
    const val USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:109.0) Gecko/20100101 Firefox/115.0"
    const val BASE_URL = "https://embtaku.pro/"
    const val BASE_URL_DUB = "recently-added-dub"

    const val ERROR_MESSAGE_ON_API_FAILURE = "Could not load data due to an error. Please try later."

    const val SEARCH_WITH_KEYWORD = "search.html"

    // the fragment initialization parameters
    const val GO_BACK_LISTENER = "goBackListener"
    const val ANIME_LIST_ITEM = "animeListItem"

    const val WEB_VIEW_REDIRECT_DOWNLOAD_PAGE_URL_PREFIX = "https://embtaku.pro/download?id="
    const val WEB_VIEW_REDIRECT_DOWNLOAD_VIDEO_URL_PREFIX = "https://gredirect.info/download.php?url="

    const val WEB_VIEW_FULLSCREEN_RATIO = "1:0" // 1:0 is for Full Screen
    const val WEB_VIEW_NORMAL_WINDOW_RATIO = "16:10.59" //correct ratio for the video frame used - to avoid scroll in view and show all controls
}