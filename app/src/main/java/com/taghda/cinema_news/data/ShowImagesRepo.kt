package com.taghda.cinema_news.data

import androidx.lifecycle.LiveData
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.taghda.cinema_news.model.Search
import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow

interface ShowImagesRepo {
    fun letDoggoImagesFlow(pagingConfig: PagingConfig, show_searcch_key: String): Flow<PagingData<Search>>
    fun letDoggoImagesObservable(pagingConfig: PagingConfig , show_searcch_key:String): Observable<PagingData<Search>>
    fun letDoggoImagesLiveData(pagingConfig: PagingConfig , show_searcch_key:String): LiveData<PagingData<Search>>
    fun letDoggoImagesFlowDb(pagingConfig: PagingConfig, show_searcch_key: String): Flow<PagingData<Search>>
    fun getDefaultPageConfig(): PagingConfig
}