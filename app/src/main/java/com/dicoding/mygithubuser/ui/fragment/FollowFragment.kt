package com.dicoding.mygithubuser.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mygithubuser.data.response.GithubUserItem
import com.dicoding.mygithubuser.databinding.FragmentFollowBinding
import com.dicoding.mygithubuser.ui.GithubAdapter
import com.dicoding.mygithubuser.viewmodel.FollowViewModel


class FollowFragment : Fragment() {

    private var _fragmentFollowBinding: FragmentFollowBinding? = null
    private val fragmentFollowBinding get() = _fragmentFollowBinding!!

    private lateinit var followViewModel: FollowViewModel
    private lateinit var adapter: GithubAdapter

    companion object {
        const val USERNAME = "username"
        const val POSITION = "position"
    }

    private var position: Int = 0
    private var username: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            position = it.getInt(POSITION)
            username = it.getString(USERNAME) ?: ""
        }

        with(fragmentFollowBinding) {
            githubFollowFragment.layoutManager = LinearLayoutManager(requireActivity())
            adapter = GithubAdapter()
            githubFollowFragment.adapter = adapter
        }

        when (position) {
            0 -> {
                followViewModel.getGithubFollow(username, 0)
            } else -> {
                followViewModel.getGithubFollow(username, 1)
            }
        }

        followViewModel.githubFollowList.observe(requireActivity()) {
            setGithubListData(it)
        }

        followViewModel.isLoading.observe(requireActivity()) {
            showLoading(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentFollowBinding = FragmentFollowBinding.inflate(layoutInflater)
        followViewModel = ViewModelProvider(this)[FollowViewModel::class.java]
        return fragmentFollowBinding.root
    }

    private fun setGithubListData(githubList: List<GithubUserItem>) {
        adapter.setGithubList(githubList)
        fragmentFollowBinding.githubFollowFragment.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        fragmentFollowBinding.progressBarFragment.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}