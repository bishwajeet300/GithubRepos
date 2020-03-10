package com.bishwajeet.githubrepos.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.bishwajeet.githubrepos.api.NetworkingInterface
import com.bishwajeet.githubrepos.api.getRepoList
import com.bishwajeet.githubrepos.db.GitHubRepositoryDataCache
import com.bishwajeet.githubrepos.model.GitHubRepository

class GitHubRepositoryBoundaryCallback(
    private val service: NetworkingInterface,
    private val local: GitHubRepositoryDataCache
) : PagedList.BoundaryCallback<GitHubRepository>() {

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false

    // keep the last requested page. When the request is successful, increment the page number.
    private var lastRequestedPage = 1

    // LiveData of network errors.
    private val _networkErrors = MutableLiveData<String>()

    // LiveData of network errors.
    val networkErrors: LiveData<String>
        get() = _networkErrors


    override fun onZeroItemsLoaded() {
        println("onZeroItemsLoaded")
        requestAndSaveData()
    }


    override fun onItemAtEndLoaded(itemAtEnd: GitHubRepository) {
        println("onItemAtEndLoaded $itemAtEnd")
        requestAndSaveData()
    }


    private fun requestAndSaveData() {
        if (isRequestInProgress) return

        isRequestInProgress = true

        getRepoList(service, lastRequestedPage, NETWORK_PAGE_SIZE, { repos ->
            if (repos.isNotEmpty()) {
                local.insert(repos) {
                    lastRequestedPage++
                    println("local.insert $lastRequestedPage")
                    isRequestInProgress = false
                }
            }

        }, { error ->
            _networkErrors.postValue(error)
            println("_networkErrors $error")
            isRequestInProgress = false
        })
    }


    companion object {
        private const val NETWORK_PAGE_SIZE = 100
    }
}