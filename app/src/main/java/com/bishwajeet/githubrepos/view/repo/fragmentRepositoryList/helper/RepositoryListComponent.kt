package com.bishwajeet.githubrepos.view.repo.fragmentRepositoryList.helper

import com.bishwajeet.githubrepos.view.repo.fragmentRepositoryList.RepositoryListFragment
import dagger.Subcomponent

@Subcomponent(modules = [RepositoryListModule::class])
interface RepositoryListComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): RepositoryListComponent
    }


    fun inject(fragment: RepositoryListFragment)
}