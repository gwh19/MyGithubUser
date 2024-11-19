package com.dicoding.mygithubuser.repository

import android.app.Application
import com.dicoding.mygithubuser.database.Favorite
import com.dicoding.mygithubuser.database.FavoriteDao
import com.dicoding.mygithubuser.database.FavoriteDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {

    private val favoriteDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteDatabase.getDatabase(application)
        favoriteDao = db.favoriteDao()
    }

    fun insertGithubFavorite(favoriteUser: Favorite) {
        executorService.execute { favoriteDao.insertGithubFavorite(favoriteUser) }
    }

    fun deleteGithubFavorite(favoriteUser: Favorite) {
        executorService.execute { favoriteDao.deleteGithubFavorite(favoriteUser) }
    }

    fun getGithubFavoriteUsername(username:String) = favoriteDao.getGithubUsernameFavorite(username)

    fun getAllGithubFavorite() = favoriteDao.getGithubFavorite()
}