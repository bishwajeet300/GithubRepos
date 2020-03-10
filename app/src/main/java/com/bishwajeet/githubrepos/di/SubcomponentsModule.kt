package com.bishwajeet.githubrepos.di

import com.bishwajeet.githubrepos.view.login.fragmentLogin.helper.LoginComponent
import dagger.Module

@Module(
    subcomponents = [
        LoginComponent::class
    ]
)
object SubcomponentsModule