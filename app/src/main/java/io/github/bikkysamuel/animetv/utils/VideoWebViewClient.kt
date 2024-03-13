package io.github.bikkysamuel.animetv.utils

import android.content.Intent
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import io.github.bikkysamuel.animetv.listeners.callbacks.VideoWebViewClientCallbackListener

class VideoWebViewClient(private val videoWebViewClientCallbackListener: VideoWebViewClientCallbackListener) : WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        val redirectUrl: String = request.url.toString()

        if (redirectUrl.startsWith(Constants.WEB_VIEW_REDIRECT_DOWNLOAD_VIDEO_URL_PREFIX)) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.setType("*/*")
            intent.putExtra(Intent.EXTRA_TEXT, redirectUrl)
            // Create intent to show chooser
            val intentChooser = Intent.createChooser(intent, /* title */ "Download file using....")
            videoWebViewClientCallbackListener.startAppChooserActivity(intentChooser = intentChooser)
        }

        if (redirectUrl.startsWith(Constants.WEB_VIEW_REDIRECT_DOWNLOAD_PAGE_URL_PREFIX)) {
            videoWebViewClientCallbackListener.expandWebView()
        }

        return !(redirectUrl.startsWith(Constants.WEB_VIEW_REDIRECT_DOWNLOAD_PAGE_URL_PREFIX))
    }
}