package com.dicoding.mygithubuser.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.mygithubuser.data.response.GithubUserItem
import com.dicoding.mygithubuser.repository.FavoriteRepository

class FavoriteViewModel(application: Application): ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val favoriteRepository: FavoriteRepository = FavoriteRepository(application)

    fun getGithubFavorite() = favoriteRepository.getAllGithubFavorite()

}