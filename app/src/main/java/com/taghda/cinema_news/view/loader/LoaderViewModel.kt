package com.taghda.cinema_news.view.loader

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
import javax.inject.Inject

@ExperimentalPagingApi
class LoaderViewModel @ViewModelInject constructor(
    val repository: ShowImagesRepo
) :
    ViewModel() {

    /**
     * returning non modified PagingData<DoggoImageModel> value as opposite to remote view model
     * where we have mapped the coming values into different object
     */
    fun fetchShowImages(): Flow<PagingData<Search>> {
        return repository.letDoggoImagesFlow(repository.getDefaultPageConfig()
        , "india").cachedIn(viewModelScope)
    }

}