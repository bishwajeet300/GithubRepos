package com.bishwajeet.githubrepos.api

import com.bishwajeet.githubrepos.model.GitHubRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


fun getRepoList(
    service: NetworkingInterface,
    page: Int,
    perPage: Int,
    onSuccess: (repos: List<GitHubRepository>) -> Unit,
    onError: (error: String) -> Unit
) {

    service.getRepoList(page, perPage).enqueue(
        object : Callback<List<GitHubRepository>> {
            override fun onFailure(call: Call<List<GitHubRepository>>, t: Throwable) {
                onError(t.message ?: "unknown error")
            }

            override fun onResponse(
                call: Call<List<GitHubRepository>>,
                response: Response<List<GitHubRepository>>
            ) {
                if (response.isSuccessful) {
                    val repos = response.body()
                    if (repos != null) {
                        onSuccess(repos)
                    }
                } else {
                    onError(response.errorBody()?.string() ?: "Unknown error")
                }
            }
        }
    )
}

interface NetworkingInterface {

    @GET("repositories")
    fun getRepoList(
        @Query(value = "page") page: Int, @Query(value = "per_page") perPage: Int
    ): Call<List<GitHubRepository>>

    companion object Factory {

        private const val BASE_URL = "https://api.github.com/"

        fun create(): NetworkingInterface {

            val retrofit = retrofit2.Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()

            return retrofit.create(NetworkingInterface::class.java)
        }
    }
}