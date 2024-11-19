package com.dicoding.mygithubuser.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mygithubuser.data.response.GithubUserItem
import com.dicoding.mygithubuser.databinding.ActivityFavoriteBinding
import com.dicoding.mygithubuser.helper.ViewModelFactory
import com.dicoding.mygithubuser.viewmodel.FavoriteViewModel

class FavoriteActivity : AppCompatActivity() {

    private lateinit var favoriteBinding: ActivityFavoriteBinding
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var adapter: GithubAdapter
    private lateinit var favoriteList: List<GithubUserItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        favoriteViewModel = obtainViewModel(this)
        setContentView(favoriteBinding.root)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "Favorite"

        with(favoriteBinding) {
            githubFavoriteRv.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            adapter= GithubAdapter()
            githubFavoriteRv.adapter
        }

        favoriteViewModel.getGithubFavorite().observe(this) {
            val addList = mutableListOf<GithubUserItem>()
            for (item in it) {
                addList.add(GithubUserItem(item.username, item.avatarUrl))
            }
            favoriteList = addList
            setGithubFavoriteList(favoriteList)
        }

        favoriteViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }

    private fun setGithubFavoriteList(githubList: List<GithubUserItem>) {
        adapter.setGithubList(githubList)
        favoriteBinding.githubFavoriteRv.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        favoriteBinding.progressBarFavorite.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
}