package com.taghda.cinema_news.view.loader

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.taghda.cinema_news.R
import com.taghda.cinema_news.view.loader.adapter.LoaderShowImageAdapter
import com.taghda.cinema_news.view.loader.adapter.LoaderStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@ExperimentalPagingApi
@AndroidEntryPoint
class LoaderFragment : Fragment(R.layout.fragment_loader) {

    lateinit var rvShowsLoader: RecyclerView
    val loaderViewModel: LoaderViewModel by viewModels()
    lateinit var adapter: LoaderShowImageAdapter
    lateinit var loaderStateAdapter: LoaderStateAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMembers()
        setUpViews(view)
        fetchDoggoImages()
    }

    private fun fetchDoggoImages() {
        lifecycleScope.launch {
            loaderViewModel.fetchShowImages().distinctUntilChanged().collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun initMembers() {
        //loaderViewModel = defaultViewModelProviderFactory.create(LoaderViewModel::class.java)
        adapter = LoaderShowImageAdapter()
        loaderStateAdapter = LoaderStateAdapter { adapter.retry() }
    }

    private fun setUpViews(view: View) {
        rvShowsLoader = view.findViewById(R.id.rvDoggoLoader)
        rvShowsLoader.layoutManager = GridLayoutManager(context, 1)
        rvShowsLoader.adapter = adapter.withLoadStateFooter(loaderStateAdapter)
    }
}