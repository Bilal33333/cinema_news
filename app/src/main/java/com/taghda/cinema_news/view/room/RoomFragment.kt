package com.taghda.cinema_news.view.room

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.taghda.cinema_news.R
import com.taghda.cinema_news.view.loader.adapter.LoaderShowImageAdapter
import com.taghda.cinema_news.view.loader.adapter.LoaderStateAdapter
import com.taghda.cinema_news.view.room.adapter.RoomShowImageAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
@AndroidEntryPoint
@ExperimentalPagingApi
class RoomFragment : Fragment(R.layout.fragment_room) {

    lateinit var recyclerView: RecyclerView
    lateinit var searchEditText: EditText
    val roomViewModel: RoomViewModel by viewModels()
    lateinit var adapter: RoomShowImageAdapter
    lateinit var loaderStateAdapter: LoaderStateAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMembers()
        setUpViews(view)
        fetchDoggoImages()
        initSearch()
    }

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

    private fun initSearch() {
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updatedSubredditFromInput()
                true
            } else {
                false
            }
        }
        searchEditText.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updatedSubredditFromInput()
                true
            } else {
                false
            }
        }
    }

    private fun updatedSubredditFromInput() {

        searchEditText.text.trim().toString().let {
            roomViewModel.searched_key = it
            fetchDoggoImages()
        }
    }

}
