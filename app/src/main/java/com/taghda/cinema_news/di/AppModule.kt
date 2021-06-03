package com.taghda.cinema_news.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import com.taghda.cinema_news.repo.paging.ShowImagesRepository
import com.taghda.cinema_news.db.AppDatabase
import com.taghda.cinema_news.api.apiHelper
import com.taghda.cinema_news.api.ShowApiService
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
    fun providesShowApiService(): ShowApiService {
        return apiHelper.getRetrofit().create(ShowApiService::class.java)
    }

    @Singleton
    @Provides
    fun providesDBinstace(@ApplicationContext context: Context): AppDatabase {
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
