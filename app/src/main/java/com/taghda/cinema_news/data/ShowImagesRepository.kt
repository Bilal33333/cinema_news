package com.taghda.cinema_news.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.*
import androidx.paging.rxjava2.observable
import com.taghda.cinema_news.model.Search
import com.taghda.cinema_news.repository.local.AppDatabase
import com.taghda.cinema_news.repository.remote.ShowApiService
import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * repository class to manage the data flow and map it if needed
 */

@ExperimentalPagingApi
class ShowImagesRepository @Inject constructor(
    var showApiService: ShowApiService,
    val appDatabase: AppDatabase
) : ShowImagesRepo{

    companion object {
        const val DEFAULT_PAGE_INDEX = 1
        const val DEFAULT_PAGE_SIZE = 2 // control nothing!

    }

    override fun letDoggoImagesFlow(pagingConfig: PagingConfig, show_searcch_key: String): Flow<PagingData<Search>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { ShowImagePagingSource(showApiService, show_searcch_key) }
        ).flow
    }

    //for rxjava users
    override fun letDoggoImagesObservable(pagingConfig: PagingConfig,
                                          show_searcch_key:String): Observable<PagingData<Search>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { ShowImagePagingSource(showApiService, show_searcch_key) }
        ).observable
    }

    //for live data users
    override fun letDoggoImagesLiveData(pagingConfig: PagingConfig, show_searcch_key:String)
    : LiveData<PagingData<Search>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { ShowImagePagingSource(showApiService, show_searcch_key) }
        ).liveData
    }

    /**
     * let's define page size, page size is the only required param, rest is optional
     */
    override fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(pageSize = DEFAULT_PAGE_SIZE, enablePlaceholders = true)
    }


    override fun letDoggoImagesFlowDb(pagingConfig: PagingConfig, show_searcch_key: String): Flow<PagingData<Search>> {
        if (appDatabase == null) throw IllegalStateException("Database is not initialized")

            val pagingSourceFactory = { appDatabase.getShowsDao().getAllShowModel() }
            var dm = ShowMediator(showApiService, appDatabase, show_searcch_key)

            return Pager(
                config = pagingConfig,
                pagingSourceFactory = pagingSourceFactory,
                remoteMediator = dm
            ).flow

    }

}