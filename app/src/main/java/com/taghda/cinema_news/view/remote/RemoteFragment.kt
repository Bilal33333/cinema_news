package com.taghda.cinema_news.view.remote

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.taghda.cinema_news.R
import com.taghda.cinema_news.view.room.adapter.RoomShowImageAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * View to fetch the results from the remote api and directly shows in the recyclerview
 * with lazy pagination enabled
 */

@ExperimentalPagingApi
@AndroidEntryPoint
class RemoteFragment : Fragment(R.layout.fragment_remote) {

    lateinit var rvShowsRemote: RecyclerView
    val remoteViewModel: RemoteViewModel by viewModels()
    lateinit var adapter: RoomShowImageAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMembers()
        setUpViews(view)
        fetchDoggoImages()
    }

    private fun fetchDoggoImages() {
        lifecycleScope.launch {
            remoteViewModel.fetchDoggoImages().distinctUntilChanged().collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun initMembers() {
      //  remoteViewModel = defaultViewModelProviderFactory.create(RemoteViewModel::class.java)
        adapter = RoomShowImageAdapter()
    }

    private fun setUpViews(view: View) {
        rvShowsRemote = view.findViewById(R.id.rvDoggoRemote)
        rvShowsRemote.layoutManager = GridLayoutManager(context, 1)
        rvShowsRemote.adapter = adapter
    }
}