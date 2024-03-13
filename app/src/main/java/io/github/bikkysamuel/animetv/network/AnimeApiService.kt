package io.github.bikkysamuel.animetv.network

import io.github.bikkysamuel.animetv.utils.Constants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface AnimeApiService {

    @GET
    fun getAnimeHomeDataWithPageNumber(@Url dubIfRequired: String, @Query("page") pageNumber: String): Call<String>

    @GET
    fun getVideoSelectedData(@Url videoUrl: String): Call<String>

    @GET(Constants.SEARCH_WITH_KEYWORD)
    fun getSearchWithKeywords(@Query("keyword") keyword: String, @Query("page") pageNumber: Int): Call<String>

}
