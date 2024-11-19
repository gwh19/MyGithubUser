package com.dicoding.mygithubuser.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.mygithubuser.R
import com.dicoding.mygithubuser.data.response.GithubUserDetail
import com.dicoding.mygithubuser.database.Favorite
import com.dicoding.mygithubuser.databinding.ActivityDetailBinding
import com.dicoding.mygithubuser.helper.ViewModelFactory
import com.dicoding.mygithubuser.viewmodel.DetailViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var detailBinding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private var githubUsernameFavorite: Favorite? = null

    private var isFabVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = ActivityDetailBinding.inflate(layoutInflater)
        detailViewModel = obtainViewModel(this)
        setContentView(detailBinding.root)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "User Detail"

        val username = intent.getStringExtra(EXTRA_USERNAME) ?: return
        detailViewModel.getGithubUserDetail(username)

        val sectionPageAdapter = SectionPagerAdapter(this)
        sectionPageAdapter.username = username
        detailBinding.githubViewPager.adapter = sectionPageAdapter

        TabLayoutMediator(detailBinding.githubTabLayout, detailBinding.githubViewPager) { tabs, position ->
            tabs.text = resources.getString(TAB_TITLES[position])
        }.attach()

        with(detailBinding) {
            githubAddFavorites.visibility = View.GONE
            githubAddShare.visibility = View.GONE
            githubAddActions.shrink()
        }

        detailBinding.githubAddActions.setOnClickListener { addFabOnClick() }

        detailBinding.githubAddShare.setOnClickListener {
            val intent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "https://github.com/${username}")
                type = "text/plain"
            }
            startActivity(intent)
        }

        detailViewModel.getGithubFavoriteUsername(username).observe(this) {
            githubUsernameFavorite = it
            if (githubUsernameFavorite == null) {
                detailBinding.githubAddFavorites.setImageResource(R.drawable.baseline_favorite_border_24)
                return@observe
            }
            detailBinding.githubAddFavorites.setImageResource(R.drawable.baseline_favorite_24)
        }

        detailBinding.githubAddFavorites.setOnClickListener {
            val githubFavorite = Favorite(username, detailViewModel.githubDetail.value?.avatarUrl)

            if (githubUsernameFavorite == null) {
                detailViewModel.insertGithubFavorite(githubFavorite)
                return@setOnClickListener
            }
            detailViewModel.deleteGithubFavorite(githubFavorite)
        }

        detailViewModel.githubDetail.observe(this) {
            getGithubDetail(it)
        }

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailViewModel.snackbarDetail.observe(this) { event ->
            event?.getContentIfNotHandled()?.let { snackbarDetail ->
                Snackbar.make(
                    window.decorView.rootView,
                    snackbarDetail,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[DetailViewModel::class.java]
    }
    private fun getGithubDetail(githubDetail: GithubUserDetail) {
        detailBinding.apply {
            githubNameDetail.text = githubDetail.name
            githubIdDetail.text = githubDetail.login
            githubFollowersDetail.text = getString(R.string.github_followers, githubDetail.followers)
            githubFollowingDetail.text = getString(R.string.github_following, githubDetail.following)
        }
        Glide.with(this)
            .load(githubDetail.avatarUrl)
            .into(detailBinding.githubAvatarDetail)
    }

    private fun showLoading(isLoading: Boolean) {
        if(isLoading) {
            detailBinding.apply {
                progressBarDetail.visibility = View.VISIBLE
                githubNameDetail.visibility = View.GONE
                githubIdDetail.visibility = View.GONE
                githubFollowersDetail.visibility = View.GONE
                githubFollowingDetail.visibility = View.GONE
            }
        } else {
            detailBinding.apply {
                progressBarDetail.visibility = View.GONE
                githubNameDetail.visibility = View.VISIBLE
                githubIdDetail.visibility = View.VISIBLE
                githubFollowersDetail.visibility = View.VISIBLE
                githubFollowingDetail.visibility = View.VISIBLE
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

    private fun addFabOnClick() {
        isFabVisible = if (!isFabVisible) {
            detailBinding.githubAddFavorites.show()
            detailBinding.githubAddShare.show()
            detailBinding.githubAddActions.extend()
            true
        } else {
            detailBinding.githubAddFavorites.hide()
            detailBinding.githubAddShare.hide()
            detailBinding.githubAddActions.shrink()
            false
        }
    }

    companion object {
        const val EXTRA_USERNAME = "username"
        private val TAB_TITLES = intArrayOf(
            R.string.github_followers_detail, R.string.github_following_detail
        )
    }
}