package io.gitlab.dwikiriyadi.hackernews.data

import HackerNewsPositionalDataSource
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import io.gitlab.dwikiriyadi.hackernews.data.model.Item

class HackerNewsDataSourceFactory(private val favorites: List<Int>) : DataSource.Factory<Int, Item>() {
    val source = MutableLiveData<HackerNewsPositionalDataSource>()
    override fun create(): DataSource<Int, Item> {
        val source = HackerNewsPositionalDataSource(favorites)
        this.source.postValue(source)
        return source
    }
}