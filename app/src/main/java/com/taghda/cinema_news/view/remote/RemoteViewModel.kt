package com.taghda.cinema_news.view.remote

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.rxjava2.cachedIn
import com.taghda.cinema_news.di.ShowImagesRepo
import com.taghda.cinema_news.model.Search
import io.reactivex.Observable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalPagingApi
class RemoteViewModel @ViewModelInject constructor(
    val repository: ShowImagesRepo
    ) : ViewModel() {

    /**
     * we just mapped the data received from the repository to [PagingData<String>] to show the map
     * function you can always return the original model if needed, in our case it would be [DoggoImageModel]
     */
    fun fetchDoggoImages(): Flow<PagingData<Search>> {
        return repository.letDoggoImagesFlow(
            repository.getDefaultPageConfig(), "indiana"
        ).cachedIn(viewModelScope)
    }

    //rxjava use case
    @ExperimentalCoroutinesApi
    fun fetchDoggoImagesObservable(): Observable<PagingData<Search>> {
        return repository.letDoggoImagesObservable(
            repository.getDefaultPageConfig(), "indiana"
        ).cachedIn(viewModelScope)
    }

    //live data use case
    fun fetchDoggoImagesLiveData(): LiveData<PagingData<Search>> {
        return repository.letDoggoImagesLiveData(
            repository.getDefaultPageConfig(), "indiana"
        ).cachedIn(viewModelScope)
    }

}