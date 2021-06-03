package com.taghda.cinema_news.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json


data class ShowImageModel(
    @Json(name= "Search")var Search: List<Search>,
    @Json(name= "Response")var Response: String
)