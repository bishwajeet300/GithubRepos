package com.bishwajeet.githubrepos.di

import android.content.Context
import com.bishwajeet.githubrepos.api.NetworkingInterface
import com.bishwajeet.githubrepos.view.login.fragmentLogin.helper.LoginComponent
import com.bishwajeet.githubrepos.view.repo.fragmentRepositoryList.RepositoryDetailComponent
import com.bishwajeet.githubrepos.view.repo.fragmentRepositoryList.helper.RepositoryListComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(
    modules = [ApplicationModule::class, ViewModelBuilderModule::class, SubcomponentsModule::class]
)
interface ApplicationComponent {

    /*
    * Makes DependencyGraph ito be available to other Android framework classes
    * */
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ApplicationComponent
    }


    /*
    * Components & SubComponents
    * */
    fun loginComponent(): LoginComponent.Factory

    fun repositoryListComponent(): RepositoryListComponent.Factory

    fun repositoryDetailComponent(): RepositoryDetailComponent.Factory


    /*
    * Return single instance
    * */
    val networkService: NetworkingInterface
}