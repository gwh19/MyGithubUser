package com.dicoding.mygithubuser.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mygithubuser.R
import com.dicoding.mygithubuser.data.response.GithubUserItem
import com.dicoding.mygithubuser.databinding.ActivityMainBinding
import com.dicoding.mygithubuser.helper.SettingPreference
import com.dicoding.mygithubuser.helper.ViewModelFactorySettingPreferences
import com.dicoding.mygithubuser.helper.dataStore
import com.dicoding.mygithubuser.viewmodel.MainViewModel
import com.dicoding.mygithubuser.viewmodel.SettingViewModel
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: GithubAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {

            githubRv.layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = GithubAdapter()
            githubRv.adapter = adapter

            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBar.text = searchView.text
                    searchView.hide()
                    if(searchView.text?.isEmpty() == true) {
                        mainViewModel.getGithubListData()
                    } else {
                        mainViewModel.getGithubSearchData(searchView.text.toString())
                    }
                    false
                }
        }

        val pref = SettingPreference.getInstance(application.dataStore)

        val settingViewModel = ViewModelProvider(this, ViewModelFactorySettingPreferences(pref))[SettingViewModel::class.java]
        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        mainViewModel.githubList.observe(this) {
            setGithubListData(it)
        }

        mainViewModel.snackbarMain.observe(this) { event ->
            event?.getContentIfNotHandled()?.let { snackbarMain ->
                Snackbar.make(
                    window.decorView.rootView,
                    snackbarMain,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setGithubListData(githubList: List<GithubUserItem>) {
        adapter.setGithubList(githubList)
        binding.githubRv.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBarMain.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        menuInflater.inflate(R.menu.menu_favorite, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        return when (item.itemId) {
            R.id.user_favorite -> {
                val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(intent)
                true
            }

            R.id.user_setting -> {
                val intent = Intent(this@MainActivity, SettingActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}