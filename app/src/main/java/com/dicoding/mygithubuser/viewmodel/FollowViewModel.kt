package com.dicoding.mygithubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.mygithubuser.data.response.GithubUserItem
import com.dicoding.mygithubuser.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel: ViewModel() {

    private val _githubFollowList = MutableLiveData<List<GithubUserItem>>()
    val githubFollowList: LiveData<List<GithubUserItem>> = _githubFollowList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "FollowViewModel"
    }

    fun getGithubFollow(username: String, position: Int) {
        _isLoading.value = true
        val client = when (position) {
            0 -> ApiConfig.getApiService().getGithubFollowers(username)
            else -> ApiConfig.getApiService().getGithubFollowing(username)
        }
        client.enqueue(object : Callback<List<GithubUserItem>> {
            override fun onResponse(
                call: Call<List<GithubUserItem>>,
                response: Response<List<GithubUserItem>>
            ) {
                if (response.isSuccessful) {
                    _githubFollowList.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
                _isLoading.value = false
            }

            override fun onFailure(
                call: Call<List<GithubUserItem>>,
                t: Throwable
            ) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                _isLoading.value = false
            }
        })
    }
}