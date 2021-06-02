package com.taghda.cinema_news.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json


data class ShowImageModel(
    @Json(name= "Search")var Search: List<Search>,
    @Json(name= "Response")var Response: String
)
@Entity
    data class Search(
        @PrimaryKey @Json(name= "imdbID") var imdbID: String,
        @Json(name= "Poster") var Poster: String,
        @Json(name= "Title") var Title: String,
        @Json(name= "Year") var Year: String,
        @Json(name= "Type") var Type: String
)