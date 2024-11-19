package com.dicoding.mygithubuser.data.response

import com.google.gson.annotations.SerializedName

data class GithubResponse(

	@field:SerializedName("items")
	val githubUser: List<GithubUserItem>? = null
)

data class GithubUserItem(

	@field:SerializedName("login")
	val githubUserName: String? = null,

	@field:SerializedName("avatar_url")
	val avatarUrl: String? = null
)
