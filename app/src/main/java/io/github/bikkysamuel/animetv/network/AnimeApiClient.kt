package io.github.bikkysamuel.animetv.network

import io.github.bikkysamuel.animetv.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

/**
 * Use the Retrofit builder to build a retrofit object
 *///using a kotlinx.serialization converter
private val retrofit = Retrofit.Builder()
//        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(Constants.BASE_URL)
    .build()


/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object AnimeApiClient {
    val retrofitService: AnimeApiService by lazy {
        retrofit.create(AnimeApiService::class.java)
    }
}