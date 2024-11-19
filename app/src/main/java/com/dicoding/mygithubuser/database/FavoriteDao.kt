package com.dicoding.mygithubuser.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.mygithubuser.database.Favorite

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertGithubFavorite(favoriteUser : Favorite)

    @Delete
    fun deleteGithubFavorite(favoriteUser : Favorite)

    @Query("SELECT * FROM favorite")
    fun getGithubFavorite(): LiveData<List<Favorite>>

    @Query("SELECT * FROM Favorite WHERE username = :username")
    fun getGithubUsernameFavorite(username: String): LiveData<Favorite>
}