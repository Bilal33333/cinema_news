package com.taghda.cinema_news.api

import com.taghda.cinema_news.model.ShowImageModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ShowApiService {

    @GET("/?apikey=c22c7cde")
    suspend fun getShowImages(
        @Query("page") page: Int,
        @Query("limit") size: Int,
        @Query("s") show_search_key: String
    ): ShowImageModel

    @GET("/?apikey=c22c7cde")
    fun getShowImages_for_test(
        @Query("page") page: Int,
        @Query("limit") size: Int,
        @Query("s") show_search_key: String
    ): Call<ResponseBody>


}