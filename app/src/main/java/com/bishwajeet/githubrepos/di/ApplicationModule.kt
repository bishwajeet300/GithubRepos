package com.bishwajeet.githubrepos.di

import android.content.Context
import com.bishwajeet.githubrepos.api.NetworkingInterface
import com.bishwajeet.githubrepos.db.ApplicationDatabase
import com.bishwajeet.githubrepos.db.GitHubRepositoryDataCache
import dagger.Module
import dagger.Provides
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
abstract class ApplicationModule {

    @Module
    companion object {

        @JvmStatic
        @Singleton
        @Provides
        fun provideAPIService(): NetworkingInterface {
            return NetworkingInterface.create()
        }


        @JvmStatic
        @Singleton
        @Provides
        fun provideRepositoryCache(context: Context): GitHubRepositoryDataCache {
            val database = ApplicationDatabase.getDatabase(context)
            return GitHubRepositoryDataCache(
                database.githubRepositoryDAO(),
                Executors.newSingleThreadExecutor()
            )
        }
    }
}