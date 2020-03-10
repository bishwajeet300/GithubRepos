package com.bishwajeet.githubrepos.view.repo.fragmentRepositoryList

import com.bishwajeet.githubrepos.view.repo.fragmentRepositoryDetail.RepositoryDetailFragment
import dagger.Subcomponent

@Subcomponent(modules = [RepositoryDetailModule::class])
interface RepositoryDetailComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): RepositoryDetailComponent
    }


    fun inject(fragment: RepositoryDetailFragment)
}