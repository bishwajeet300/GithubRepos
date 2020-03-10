package com.bishwajeet.githubrepos.view.repo.fragmentRepositoryList.helper

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bishwajeet.githubrepos.model.GitHubRepository

class RepositoryListAdapter(private val listener: RepoClickListener): PagedListAdapter<GitHubRepository, RecyclerView.ViewHolder>(
    REPO_COMPARATOR
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RepositoryListItemViewHolder.create(
            parent,
            listener
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val repoItem = getItem(position)
        if (repoItem != null) {
            (holder as RepositoryListItemViewHolder).bind(repoItem)
        }
    }


    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<GitHubRepository>() {
            override fun areItemsTheSame(oldItem: GitHubRepository, newItem: GitHubRepository): Boolean =
                oldItem.fullName == newItem.fullName

            override fun areContentsTheSame(oldItem: GitHubRepository, newItem: GitHubRepository): Boolean =
                oldItem == newItem
        }
    }
}