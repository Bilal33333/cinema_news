package com.taghda.cinema_news.view.room.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.taghda.cinema_news.R
import com.taghda.cinema_news.model.Search

class RoomShowImageAdapter :
    PagingDataAdapter<Search, RoomShowImageAdapter.ShowsImageViewHolder>(REPO_COMPARATOR) {

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Search>() {
            override fun areItemsTheSame(oldItem: Search, newItem: Search) =
                oldItem.imdbID == newItem.imdbID

            override fun areContentsTheSame(oldItem: Search, newItem: Search) =
                oldItem.imdbID == newItem.imdbID
        }
    }

    override fun onBindViewHolder(holder: ShowsImageViewHolder, position: Int) {
        holder.bind(item = getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowsImageViewHolder {
        return ShowsImageViewHolder.getInstance(parent)
    }

    /**
     * view holder class for doggo item
     */
    class ShowsImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        companion object {
            //get instance of the DoggoImageViewHolder
            fun getInstance(parent: ViewGroup): ShowsImageViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val view = inflater.inflate(R.layout.item_doggo_image_view, parent, false)
                return ShowsImageViewHolder(view)
            }
        }

        var ivDoggoMain: ImageView = view.findViewById(R.id.ivDoggoMain)
        var ivtitle: TextView = view.findViewById(R.id.ivtitle)
        var ivyear: TextView = view.findViewById(R.id.ivyear)
        var ivtype: TextView = view.findViewById(R.id.ivtype)

        fun bind(item: Search?) {
            //loads image from network using coil extension function
            ivDoggoMain.load(item?.Poster) { placeholder(R.drawable.doggo_placeholder) }
            ivtitle.setText("title: "+item?.Title)
            ivtype.setText("type: "+item?.Type)
            ivyear.setText("year: "+item?.Year)
        }
    }

}