package com.taghda.cinema_news.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.taghda.cinema_news.model.Search

@Dao
interface SearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(search: List<Search>)

    @Query("SELECT * FROM Search")
    fun getAllShowModel(): PagingSource<Int, Search>

    @Query("DELETE FROM Search")
    suspend fun clearAllDoggos()

}