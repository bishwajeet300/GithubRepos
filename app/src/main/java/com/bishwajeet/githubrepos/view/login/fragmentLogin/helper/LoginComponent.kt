package com.bishwajeet.githubrepos.view.login.fragmentLogin.helper

import com.bishwajeet.githubrepos.view.login.fragmentLogin.LoginFragment
import dagger.Subcomponent

@Subcomponent(modules = [LoginModule::class])
interface LoginComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): LoginComponent
    }


    fun inject(fragment: LoginFragment)
}