package com.bishwajeet.githubrepos.data

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import com.bishwajeet.githubrepos.api.NetworkingInterface
import com.bishwajeet.githubrepos.db.GitHubRepositoryDataCache
import com.bishwajeet.githubrepos.model.GitHubRepositoryResult
import javax.inject.Inject

class GitHubRepositoryRepository @Inject constructor(
    private val service: NetworkingInterface,
    private val local: GitHubRepositoryDataCache
) {

    fun getRepos(): GitHubRepositoryResult {
        // Get data source factory from the local cache
        val dataSourceFactory = local.getRepos()
        val boundaryCallback = GitHubRepositoryBoundaryCallback(service, local)
        val networkErrors = boundaryCallback.networkErrors

        // Get the paged list
        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
            .setBoundaryCallback(boundaryCallback)
            .build()

        // Get the network errors exposed by the boundary callback
        return GitHubRepositoryResult(data, networkErrors)
    }


    fun getRepoCount(): LiveData<Int> {
        return local.getRepoCount()
    }


    companion object {
        private const val DATABASE_PAGE_SIZE = 20
    }
}