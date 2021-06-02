package com.taghda.cinema_news.view.room

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.taghda.cinema_news.data.ShowImagesRepo
import com.taghda.cinema_news.data.ShowImagesRepository
import com.taghda.cinema_news.model.Search
import kotlinx.coroutines.flow.Flow

@ExperimentalPagingApi
class RoomViewModel @ViewModelInject constructor(
    val repository: ShowImagesRepo) :
    ViewModel() {
     var searched_key: String= "russia"

    fun fetchShowsImages(): Flow<PagingData<Search>> {
       return repository.letDoggoImagesFlowDb(
           repository.getDefaultPageConfig(), searched_key).cachedIn(viewModelScope)
        }


}