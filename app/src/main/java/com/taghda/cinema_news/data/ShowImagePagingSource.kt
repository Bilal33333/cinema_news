package com.taghda.cinema_news.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.taghda.cinema_news.data.ShowImagesRepository.Companion.DEFAULT_PAGE_INDEX
import com.taghda.cinema_news.model.Search
import com.taghda.cinema_news.repository.remote.ShowApiService
import retrofit2.HttpException
import java.io.IOException

/**
 * provides the data source for paging lib from api calls
 */
@ExperimentalPagingApi
class ShowImagePagingSource(val showApiService: ShowApiService,
                            var show_searcch_key: String) :
    PagingSource<Int, Search>() {


    /**
     * calls api if there is any error getting results then return the [LoadResult.Error]
     * for successful response return the results using [LoadResult.Page] for some reason if the results
     * are empty from service like in case of no more data from api then we can pass [null] to
     * send signal that source has reached the end of list
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Search> {
        //for first case it will be null, then we can pass some default value, in our case it's 1
        val page = params.key ?: DEFAULT_PAGE_INDEX
        return try {
            val response = showApiService.getShowImages(page, params.loadSize, show_searcch_key)

            var response_search= response.Search
            LoadResult.Page(
                    response_search,
                prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1,
                nextKey = if (response_search.isEmpty()) null else page + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Search>): Int? {
        return null
    }

}