package com.taghda.cinema_news.repository.local

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import com.taghda.cinema_news.data.ShowImagesRepo
import com.taghda.cinema_news.data.ShowImagesRepository
import com.taghda.cinema_news.repository.remote.RemoteInjector
import com.taghda.cinema_news.repository.remote.ShowApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object appModule {

    @Singleton
    @Provides
    fun injectShowsApiService(): ShowApiService {
        return RemoteInjector.getRetrofit().create(ShowApiService::class.java)
    }

    @Singleton
    @Provides
    fun injectDB(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @ExperimentalPagingApi
    @Singleton
    @Provides
    fun provideMainRepository(
        showApiService: ShowApiService,
        appDatabase: AppDatabase
    ): ShowImagesRepo = ShowImagesRepository(showApiService, appDatabase)

}
