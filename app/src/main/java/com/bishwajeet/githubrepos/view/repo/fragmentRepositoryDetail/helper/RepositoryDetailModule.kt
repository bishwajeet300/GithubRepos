package com.bishwajeet.githubrepos.view.repo.fragmentRepositoryList

import androidx.lifecycle.ViewModel
import com.bishwajeet.githubrepos.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class RepositoryDetailModule {

    @Binds
    @IntoMap
    @ViewModelKey(RepositoryDetailViewModel::class)
    abstract fun bindViewModel(viewModel: RepositoryDetailViewModel): ViewModel
}