package com.dicoding.mygithubuser.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Favorite(
    @PrimaryKey(autoGenerate = false)
    var username: String = "",
    val avatarUrl: String? = null
)
