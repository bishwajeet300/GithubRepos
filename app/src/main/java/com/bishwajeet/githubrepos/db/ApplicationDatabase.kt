package com.bishwajeet.githubrepos.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bishwajeet.githubrepos.db.dao.GithubRepositoryDAO
import com.bishwajeet.githubrepos.model.GitHubRepository

@Database(entities = [GitHubRepository::class], version = 1, exportSchema = false)
abstract class ApplicationDatabase : RoomDatabase() {

    abstract fun githubRepositoryDAO(): GithubRepositoryDAO

    companion object {
        @Volatile
        private var INSTANCE: ApplicationDatabase? = null

        fun getDatabase(context: Context): ApplicationDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ApplicationDatabase::class.java,
                    "repository_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}