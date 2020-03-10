package com.bishwajeet.githubrepos.view.repo.fragmentRepositoryList

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.bishwajeet.githubrepos.data.GitHubRepositoryRepository
import com.bishwajeet.githubrepos.model.GitHubRepository
import com.bishwajeet.githubrepos.model.GitHubRepositoryResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

class RepositoryListViewModel @Inject constructor(
    private val githubRepository: GitHubRepositoryRepository
) : ViewModel() {

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val _response = MutableLiveData<GitHubRepositoryResult>()
    val _data: LiveData<PagedList<GitHubRepository>> =
        Transformations.switchMap(_response) { it -> it.data }
    val _eventNetworkError: LiveData<String> =
        Transformations.switchMap(_response) { it -> it.networkErrors }

    private val _responseCount = MutableLiveData<Int>()
    val _count: LiveData<Int>
        get() = _responseCount


    init {
        getGitHubRepositoryList()
        getRepoCount()
    }


    private fun getGitHubRepositoryList() {
        viewModelScope.launch {
            _response.postValue(githubRepository.getRepos())
            getRepoCount()
        }
    }


    private fun getRepoCount() {
        viewModelScope.launch {
            _responseCount.postValue(githubRepository.getRepoCount().value)
            Log.i(
                "getRepoCount",
                "Updating list with ${githubRepository.getRepoCount().value} items"
            )
        }
    }
}
