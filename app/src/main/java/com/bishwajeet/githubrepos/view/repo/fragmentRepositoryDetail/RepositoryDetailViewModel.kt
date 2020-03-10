package com.bishwajeet.githubrepos.view.repo.fragmentRepositoryList

import androidx.lifecycle.ViewModel
import com.bishwajeet.githubrepos.model.GitHubRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject

class RepositoryDetailViewModel @Inject constructor() : ViewModel() {

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private lateinit var data: GitHubRepository

    fun setRepository(it: GitHubRepository): Boolean {
        data = it
        return true
    }


    fun getRepository(): GitHubRepository {
        return data
    }
}
