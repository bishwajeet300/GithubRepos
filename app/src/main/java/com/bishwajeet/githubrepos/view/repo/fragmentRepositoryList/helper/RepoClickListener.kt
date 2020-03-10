package com.bishwajeet.githubrepos.view.repo.fragmentRepositoryList.helper

import com.bishwajeet.githubrepos.model.GitHubRepository

interface RepoClickListener {

    fun onRepoClick(repo: GitHubRepository)
}