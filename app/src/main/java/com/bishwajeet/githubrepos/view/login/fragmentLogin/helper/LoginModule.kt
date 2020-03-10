package com.bishwajeet.githubrepos.view.login.fragmentLogin.helper

import androidx.lifecycle.ViewModel
import com.bishwajeet.githubrepos.di.ViewModelKey
import com.bishwajeet.githubrepos.view.login.fragmentLogin.LoginViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class LoginModule {

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindViewModel(viewModel: LoginViewModel): ViewModel
}