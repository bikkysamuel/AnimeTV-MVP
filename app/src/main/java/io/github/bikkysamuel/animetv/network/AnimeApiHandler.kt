package io.github.bikkysamuel.animetv.network

import io.github.bikkysamuel.animetv.utils.Constants
import io.github.bikkysamuel.animetv.utils.MyLogger
import io.github.bikkysamuel.animetv.utils.MyUtils
import retrofit2.Call
import retrofit2.Callback

class AnimeApiHandler {

    companion object {
        private fun <T> callApi(call: Call<T>, apiResponseListener: ApiResponseListener<T>) {
            call.enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>,
                                        response: retrofit2.Response<T>)
                {
                    val body: T = response.body()!!
                    apiResponseListener.onApiResponseSuccess(body)
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    MyLogger.d(this, t.message)
                    apiResponseListener.onApiResponseFailure(Constants.ERROR_MESSAGE_ON_API_FAILURE)
                }
            })
        }

        private val retrofitService: AnimeApiService
            get() {
                return AnimeApiClient.retrofitService
            }

        fun getHomePageDataWithPageNumber(showDubVersion: Boolean, pageNumber: Int, apiResponseListener: ApiResponseListener<String>) {
            val call: Call<String> = retrofitService.getAnimeHomeDataWithPageNumber(if (showDubVersion) Constants.BASE_URL_DUB else "", pageNumber.toString())
            callApi(call, apiResponseListener)
        }

        fun getVideoPlayerData(videoUrl: String, apiResponseListener: ApiResponseListener<String>) {
            val call: Call<String> = retrofitService.getVideoSelectedData(videoUrl = videoUrl)
            callApi(call, apiResponseListener)
        }

        fun searchWithKeyword(keyword: String, pageNumber: Int, apiResponseListener: ApiResponseListener<String>) {
            val call: Call<String> = retrofitService.getSearchWithKeywords(keyword = keyword, pageNumber = pageNumber)
            callApi(call, apiResponseListener)
        }
    }
}