package com.taghda.cinema_news.repo.paging.netw_db

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.taghda.cinema_news.repo.paging.ShowImagesRepository.Companion.DEFAULT_PAGE_INDEX
import com.taghda.cinema_news.model.Search
import com.taghda.cinema_news.db.AppDatabase
import com.taghda.cinema_news.db.RemoteKeys
import com.taghda.cinema_news.api.ShowApiService
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException


@ExperimentalPagingApi
class ShowMediator(val showApiService: ShowApiService,
                   val appDatabase: AppDatabase,
                   var show_searcch_key: String
                   ) :RemoteMediator<Int, Search>() {

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, Search>
    ): MediatorResult {

        try {
            val pageKeyData = getKeyPageData(loadType, state)
            val page = when (pageKeyData) {
                is MediatorResult.Success -> {
                    return pageKeyData
                }
                else -> {
                    pageKeyData as Int
                }
            }
            val response = showApiService
                .getShowImages(page, state.config.pageSize, show_searcch_key)


            var response_search= response.Search
            val isEndOfList = response_search.isEmpty()
            appDatabase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    appDatabase.getRepoDao().clearRemoteKeys()
                    appDatabase.getShowsDao().clearAllDoggos()
                }
                val prevKey = if(page.equals(DEFAULT_PAGE_INDEX)) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1
                val keys = response_search.map {
                    RemoteKeys(repoId = it.imdbID, prevKey = prevKey, nextKey = nextKey)
                }
                appDatabase.getRepoDao().insertAll(keys)
                appDatabase.getShowsDao().insertAll(response_search)
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    /**
     * this returns the page key or the final end of list success result
     */
    suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, Search>): Any? {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getClosestRemoteKey(state)
                remoteKeys?.nextKey?.minus(1) ?: DEFAULT_PAGE_INDEX
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                    ?: throw InvalidObjectException("Remote key should not be null for $loadType")
                remoteKeys.nextKey
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                    ?: throw InvalidObjectException("Invalid state, key should not be null")
                //end of list condition reached
                remoteKeys.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                remoteKeys.prevKey
            }
        }
    }

    /**
     * get the last remote key inserted which had the data
     */
    private suspend fun getLastRemoteKey(state: PagingState<Int, Search>): RemoteKeys? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { doggo -> appDatabase.getRepoDao().remoteKeysDoggoId(doggo.imdbID) }
    }

    /**
     * get the first remote key inserted which had the data
     */
    private suspend fun getFirstRemoteKey(state: PagingState<Int, Search>): RemoteKeys? {
        return state.pages
            .firstOrNull() { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { doggo -> appDatabase.getRepoDao().remoteKeysDoggoId(doggo.imdbID) }
    }

    /**
     * get the closest remote key inserted which had the data
     */
    private suspend fun getClosestRemoteKey(state: PagingState<Int, Search>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.imdbID?.let { repoId ->
                appDatabase.getRepoDao().remoteKeysDoggoId(repoId)
            }
        }
    }

}