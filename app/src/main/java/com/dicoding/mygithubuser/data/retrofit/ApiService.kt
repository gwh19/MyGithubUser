package com.dicoding.mygithubuser.data.retrofit

import com.dicoding.mygithubuser.data.response.GithubResponse
import com.dicoding.mygithubuser.data.response.GithubUserDetail
import com.dicoding.mygithubuser.data.response.GithubUserItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    fun getGithubUser() : Call<List<GithubUserItem>>

    @GET("search/users")
    fun getGithubSearchUser(@Query("q") username : String) : Call<GithubResponse>

    @GET("users/{username}")
    fun getGithubDetailUser(@Path("username") username: String) : Call<GithubUserDetail>

    @GET("users/{username}/followers")
    fun getGithubFollowers(@Path("username") username: String) : Call<List<GithubUserItem>>

    @GET("users/{username}/following")
    fun getGithubFollowing(@Path("username") username: String) : Call<List<GithubUserItem>>
}
