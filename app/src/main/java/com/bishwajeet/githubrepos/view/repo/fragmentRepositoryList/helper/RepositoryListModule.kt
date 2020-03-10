package com.bishwajeet.githubrepos.view.repo.fragmentRepositoryList.helper

import androidx.lifecycle.ViewModel
import com.bishwajeet.githubrepos.di.ViewModelKey
import com.bishwajeet.githubrepos.view.repo.fragmentRepositoryList.RepositoryListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class RepositoryListModule {

    @Binds
    @IntoMap
    @ViewModelKey(RepositoryListViewModel::class)
    abstract fun bindViewModel(viewModel: RepositoryListViewModel): ViewModel
}