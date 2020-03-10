package com.bishwajeet.githubrepos.db.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bishwajeet.githubrepos.model.GitHubRepository

@Dao
interface GithubRepositoryDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(repos: List<GitHubRepository>)


    @Query("SELECT * FROM github_repository")
    fun getRepos(): DataSource.Factory<Int, GitHubRepository>


    @Query("SELECT COUNT(*) FROM github_repository")
    fun getRepoCount(): LiveData<Int>
}