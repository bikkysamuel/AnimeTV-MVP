package io.github.bikkysamuel.animetv.network

interface ApiResponseListener<T> {
    fun onApiResponseSuccess(responseAsHtml: T)
    fun onApiResponseFailure(errorMessage: String)
}