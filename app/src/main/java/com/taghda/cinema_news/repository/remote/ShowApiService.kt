package com.taghda.cinema_news.repository.remote

import com.taghda.cinema_news.model.ShowImageModel
import retrofit2.http.GET
import retrofit2.http.Query

interface ShowApiService {

    @GET("/?apikey=c22c7cde")
    suspend fun getShowImages(
            @Query("page") page: Int,
            @Query("limit") size: Int,
            @Query("s") show_search_key: String
    ): ShowImageModel

}