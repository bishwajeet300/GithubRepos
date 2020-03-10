package com.bishwajeet.githubrepos.model

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

data class GitHubRepositoryResult(
    val data: LiveData<PagedList<GitHubRepository>>,
    val networkErrors: LiveData<String>
)