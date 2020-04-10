package io.gitlab.dwikiriyadi.hackernews.ui.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import io.gitlab.dwikiriyadi.hackernews.BR
import io.gitlab.dwikiriyadi.hackernews.data.model.Item

class HackerNewsViewHolder(private val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(data: Item, func: () -> Unit) {
        binding.setVariable(BR.data, data)
        binding.root.setOnClickListener {
            func()
        }
        binding.executePendingBindings()
    }
}