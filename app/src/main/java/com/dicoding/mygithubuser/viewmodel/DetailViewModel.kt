package com.dicoding.mygithubuser.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.mygithubuser.data.response.GithubUserDetail
import com.dicoding.mygithubuser.data.retrofit.ApiConfig
import com.dicoding.mygithubuser.database.Favorite
import com.dicoding.mygithubuser.repository.FavoriteRepository
import com.dicoding.mygithubuser.utils.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application): ViewModel() {

    private val _githubDetail = MutableLiveData<GithubUserDetail>()
    val githubDetail: LiveData<GithubUserDetail> = _githubDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _snackbarDetail = MutableLiveData<Event<String>?>()
    val snackbarDetail: LiveData<Event<String>?> = _snackbarDetail

    private val githubFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    fun insertGithubFavorite(favoriteUser: Favorite) {
        githubFavoriteRepository.insertGithubFavorite(favoriteUser)
        _snackbarDetail.value = Event("User Berhasil Ditambah ke Halaman Favorit")
        _snackbarDetail.postValue(null)
    }

    fun deleteGithubFavorite(favoriteUser: Favorite) {
        githubFavoriteRepository.deleteGithubFavorite(favoriteUser)
        _snackbarDetail.value = Event("User Berhasil Dihapus dari Halaman Favorit")
        _snackbarDetail.postValue(null)
    }

    fun getGithubFavoriteUsername(username: String) = githubFavoriteRepository.getGithubFavoriteUsername(username)

    fun getGithubUserDetail(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getGithubDetailUser(username)
        client.enqueue(object : Callback<GithubUserDetail> {
            override fun onResponse(
                call: Call<GithubUserDetail>,
                response: Response<GithubUserDetail>
            ) {
                if(response.isSuccessful) {
                    _githubDetail.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
                _isLoading.value = false
            }

            override fun onFailure(
                call: Call<GithubUserDetail>,
                t: Throwable
            ) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                _snackbarDetail.value = Event("Gagal Memuat Halaman Detail")
                _snackbarDetail.postValue(null)
                _isLoading.value = false
            }
        })
    }

    companion object {
        private const val TAG = "DetailViewModel"
    }
}