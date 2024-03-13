package io.github.bikkysamuel.animetv.listeners.callbacks

import android.content.Intent

interface VideoWebViewClientCallbackListener {
    fun startAppChooserActivity(intentChooser: Intent)
    fun expandWebView()
}