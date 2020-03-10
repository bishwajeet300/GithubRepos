package com.bishwajeet.githubrepos.db

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.bishwajeet.githubrepos.db.dao.GithubRepositoryDAO
import com.bishwajeet.githubrepos.model.GitHubRepository
import java.util.concurrent.Executor

class GitHubRepositoryDataCache(
    private val repoDao: GithubRepositoryDAO,
    private val ioExecutor: Executor
) {
    /**
     * Insert a list of repos in the database, on a background thread.
     */
    fun insert(repos: List<GitHubRepository>, insertFinished: () -> Unit) {
        ioExecutor.execute {
            Log.d("GithubLocalCache", "inserting ${repos.size} repos")
            repoDao.insert(repos)
            insertFinished()
        }
    }

    fun getRepos(): DataSource.Factory<Int, GitHubRepository> {
        return repoDao.getRepos()
    }

    fun getRepoCount(): LiveData<Int> {
        return repoDao.getRepoCount()
    }
}