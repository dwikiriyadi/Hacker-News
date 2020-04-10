package io.gitlab.dwikiriyadi.hackernews.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import io.gitlab.dwikiriyadi.hackernews.R
import io.gitlab.dwikiriyadi.hackernews.data.model.Item

class HackerNewsAdapter(val onItemClick: (item: Item) -> Unit) :
    PagedListAdapter<Item, HackerNewsViewHolder>(REPO_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HackerNewsViewHolder {
        val binding: ViewDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            when (viewType) {
                0 -> R.layout.favorite_item
                else -> R.layout.story_item
            },
            parent,
            false
        )

        return HackerNewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HackerNewsViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item) {
                onItemClick(item)
            }
        }
    }

    override fun getItemViewType(position: Int) = if (getItem(position)!!.favorite) 0 else 1


    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item) =
                oldItem.title == newItem.title

            override fun areContentsTheSame(oldItem: Item, newItem: Item) =
                oldItem == newItem
        }
    }

}