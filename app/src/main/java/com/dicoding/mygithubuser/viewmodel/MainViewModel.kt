package com.dicoding.mygithubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.mygithubuser.data.response.GithubResponse
import com.dicoding.mygithubuser.data.response.GithubUserItem
import com.dicoding.mygithubuser.data.retrofit.ApiConfig
import com.dicoding.mygithubuser.utils.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel: ViewModel() {

    private val _githubList = MutableLiveData<List<GithubUserItem>>()
    val githubList : LiveData<List<GithubUserItem>> = _githubList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _snackbarMain = MutableLiveData<Event<String>?>()
    val snackbarMain : LiveData<Event<String>?> = _snackbarMain

    private val fetchData = false

    init {
        getGithubListData()
    }

    fun getGithubListData() {
        if (!fetchData) {
            _isLoading.value = true
            val client = ApiConfig.getApiService().getGithubUser()
            client.enqueue(object : Callback<List<GithubUserItem>> {
                override fun onResponse(
                    call: Call<List<GithubUserItem>>,
                    response: Response<List<GithubUserItem>>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _githubList.value = response.body()
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(
                    call: Call<List<GithubUserItem>>,
                    t: Throwable
                ) {
                    _isLoading.value = false
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                    _snackbarMain.value = Event("Gagal Membuat List Github")
                    _snackbarMain.postValue(null)
                }
            })
        }
    }

    fun getGithubSearchData(data: String) {
        _isLoading.value = true
        _githubList.value = emptyList()
        val client = ApiConfig.getApiService().getGithubSearchUser(data)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                if (response.body()?.githubUser?.isEmpty() != true) {
                    _githubList.value = response.body()?.githubUser!!
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _snackbarMain.value = Event("User Yang Dicari Tidak Tersedia")
                }
                _isLoading.value = false
            }

            override fun onFailure(
                call: Call<GithubResponse>,
                t: Throwable
            ) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                _snackbarMain.value = Event("Gagal Mencari User Github")
                _isLoading.value = false
            }
        })
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}