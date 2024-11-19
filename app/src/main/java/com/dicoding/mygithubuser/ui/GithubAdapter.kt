package com.dicoding.mygithubuser.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.mygithubuser.R
import com.dicoding.mygithubuser.data.response.GithubUserItem

class GithubAdapter : RecyclerView.Adapter<GithubAdapter.MyViewHolder>() {

    private var _githubList : List<GithubUserItem?>? = emptyList()

    fun setGithubList(value: List<GithubUserItem>) {
        val diffCallback = GithubUserItemDiffCallback(_githubList, value)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        _githubList = value
        diffResult.dispatchUpdatesTo(this)
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val githubName: TextView = itemView.findViewById(R.id.github_name)
        val githubAvatar: ImageView = itemView.findViewById(R.id.github_avatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.github_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return _githubList?.size?:0
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.githubName.text = _githubList?.get(position)?.githubUserName ?: ""
        Glide.with(holder.itemView.context)
            .load(_githubList?.get(position)?.avatarUrl?: "https://i.stack.imgur.com/l60Hf.png")
            .into(holder.githubAvatar)
        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_USERNAME, _githubList?.get(position)?.githubUserName)
            holder.itemView.context.startActivity(intent)
        }
    }

    class GithubUserItemDiffCallback(
        private val oldList: List<GithubUserItem?>?,
        private val newList: List<GithubUserItem?>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList?.size?: 0

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList?.get(oldItemPosition)
            val newItem = newList[newItemPosition]
            return oldItem?.githubUserName == newItem?.githubUserName
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList?.get(oldItemPosition)
            val newItem = newList[newItemPosition]
            return oldItem == newItem
        }
    }
}