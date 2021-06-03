package com.taghda.cinema_news.repo.paging

import androidx.lifecycle.LiveData
import androidx.paging.*
import androidx.paging.rxjava2.observable
import com.taghda.cinema_news.di.ShowImagesRepo
import com.taghda.cinema_news.model.Search
import com.taghda.cinema_news.db.AppDatabase
import com.taghda.cinema_news.api.ShowApiService
import com.taghda.cinema_news.repo.paging.netw.ShowImagePagingSource
import com.taghda.cinema_news.repo.paging.netw_db.ShowMediator
import io.reactivex.Observable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * repository class to manage the data flow and map it if needed
 */

@ExperimentalPagingApi
class ShowImagesRepository @Inject constructor(
    var showApiService: ShowApiService,
    val appDatabase: AppDatabase
) : ShowImagesRepo {

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
    @ExperimentalCoroutinesApi
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

        val pagingSourceFactory = { appDatabase.getShowsDao().getAllShowModel() }
            val dm = ShowMediator(showApiService, appDatabase, show_searcch_key)

            return Pager(
                config = pagingConfig,
                pagingSourceFactory = pagingSourceFactory,
                remoteMediator = dm
            ).flow

    }

}