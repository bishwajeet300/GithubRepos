package com.bishwajeet.githubrepos.view.repo.fragmentRepositoryList.helper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bishwajeet.githubrepos.R
import com.bishwajeet.githubrepos.model.GitHubRepository

class RepositoryListItemViewHolder(view: View, listener: RepoClickListener) :
    RecyclerView.ViewHolder(view) {
    private val name: TextView = view.findViewById(R.id.repo_name)
    private val description: TextView = view.findViewById(R.id.repo_description)

    private var repo: GitHubRepository? = null

    init {
        view.setOnClickListener {
            if (repo == null) {
                val resources = itemView.resources
                name.text = resources.getString(R.string.loading)
                description.visibility = View.GONE
            } else {
                listener.onRepoClick(repo!!)
            }
        }
    }

    fun bind(repo: GitHubRepository?) {
        if (repo == null) {
            val resources = itemView.resources
            name.text = resources.getString(R.string.loading)
            description.visibility = View.GONE
        } else {
            showRepoData(repo)
        }
    }

    private fun showRepoData(repo: GitHubRepository) {
        this.repo = repo
        name.text = repo.fullName

        // if the description is missing, hide the TextView
        description.text = repo.description
        val descriptionVisibility: Int = View.VISIBLE
        description.visibility = descriptionVisibility
    }

    companion object {
        fun create(parent: ViewGroup, listener: RepoClickListener): RepositoryListItemViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.repo_view_item, parent, false)
            return RepositoryListItemViewHolder(
                view,
                listener
            )
        }
    }
}