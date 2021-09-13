# cinema_news

This repository is all about Paging3 implementation for various use cases like 
- Paging using network (retrofit) as data source
- Paging using network and local db room.
- Showing footer error view or loading states.

![paging architecture](https://developer.android.com/topic/libraries/architecture/images/paging3-library-architecture.svg)

## CAN you explain with code? OK : 

[showMediator](app/src/main/java/com/taghda/cinema_news/repo/paging/netw_db/ShowMediator.kt): 

```kt

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
    
```

[showImagesRepository](app/src/main/java/com/taghda/cinema_news/repo/paging/ShowImagesRepository.kt) : 

```kt    
        override fun letDoggoImagesFlowDb(pagingConfig: PagingConfig, show_searcch_key: String): Flow<PagingData<Search>> {


        val pagingSourceFactory = { appDatabase.getShowsDao().getAllShowModel() }
            val dm = ShowMediator(showApiService, appDatabase, show_searcch_key)


            return Pager(
                config = pagingConfig,
                pagingSourceFactory = pagingSourceFactory,
                remoteMediator = dm
            ).flow

    }
```

[RoomViewModel](app/src/main/java/com/taghda/cinema_news/view/room/RoomViewModel.kt) : 

```kt
class RoomViewModel @ViewModelInject constructor(
    val repository: ShowImagesRepo
) :
    ViewModel() {
     var searched_key: String= "russia"


    fun fetchShowsImages(): Flow<PagingData<Search>> {
       return repository.letDoggoImagesFlowDb(
           repository.getDefaultPageConfig(), searched_key).cachedIn(viewModelScope)
        }

[RoomShowImageAdapter](app/src/main/java/com/taghda/cinema_news/view/room/adapter/RoomShowImageAdapter.kt):


class RoomShowImageAdapter :
    PagingDataAdapter<Search, RoomShowImageAdapter.ShowsImageViewHolder>(REPO_COMPARATOR) {
    
[RoomFragment](app/src/main/java/com/taghda/cinema_news/view/room/RoomFragment.kt) : 

    private fun fetchDoggoImages() {
        lifecycleScope.launch {
            roomViewModel.fetchShowsImages().distinctUntilChanged().collectLatest {
                adapter.submitData(it)
            }
        }
    }


    private fun initMembers() {
    //    roomViewModel = defaultViewModelProviderFactory.create(RoomViewModel::class.java)
        adapter = RoomShowImageAdapter()
        loaderStateAdapter = LoaderStateAdapter { adapter.retry() }
    }


    private fun setUpViews(view: View) {
        recyclerView = view.findViewById(R.id.rvDoggoRoom)
        searchEditText = view.findViewById(R.id.input)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter.withLoadStateFooter(loaderStateAdapter)
    }
}
```

    
## Author

[@bilaltaghda in twitter](https://twitter.com/BilalTaghda)

[@bilaltaghda in linkedin](https://www.linkedin.com/in/bilal-taghda-7892b9200/)

[@bilaltaghda5 in facebook](https://www.facebook.com/bilaltaghda5)
