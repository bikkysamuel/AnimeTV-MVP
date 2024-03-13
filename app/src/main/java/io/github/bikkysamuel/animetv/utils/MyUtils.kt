package io.github.bikkysamuel.animetv.utils

import android.webkit.WebView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import io.github.bikkysamuel.animetv.R
import io.github.bikkysamuel.animetv.models.VideoType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MyUtils {

    companion object {

        fun getAnimeTitle(name: String): String {
            val indexOfEpisodeString: Int = name.indexOf("Episode")
            return name.substring(0, if (indexOfEpisodeString >= 0) indexOfEpisodeString - 1 else name.length)
        }

        fun setDimensionRatioOnWebViewVideoPlayer(webViewVideoPlayer: WebView, dimensionRatio: String) {
            val constraintLayout: ConstraintLayout = webViewVideoPlayer.rootView.findViewById(R.id.fragment_video_player)
            val constraintSet = ConstraintSet()
            constraintSet.clone(constraintLayout)
            constraintSet.setDimensionRatio(R.id.webView_videoPlayer, dimensionRatio)
            constraintSet.applyTo(constraintLayout)
        }

        fun runSuspendFunction(codeOrFunctionToRun: suspend () -> Unit) {
            CoroutineScope(Job() + Dispatchers.Main).launch {
                codeOrFunctionToRun()
            }
        }

        fun parseVideoType(videoTypeAsString: String) : VideoType {
            return when(videoTypeAsString) {
                "SUB" -> VideoType.SUB
                "DUB" -> VideoType.DUB
                "RAW" -> VideoType.RAW
                else -> VideoType.RAW
            }
        }
    }
}