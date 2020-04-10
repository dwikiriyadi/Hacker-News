package io.gitlab.dwikiriyadi.hackernews.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.gitlab.dwikiriyadi.hackernews.data.model.Item
import io.gitlab.dwikiriyadi.hackernews.data.model.NetworkState
import io.gitlab.dwikiriyadi.hackernews.data.model.Result
import java.lang.Exception
import java.util.concurrent.Executor

class HackerNewsRepository(private val networkExecutor: Executor) {

    fun getTopStories(favorites: List<Int>): Result<PagedList<Item>> {
        val dataSourceFactory = HackerNewsDataSourceFactory(favorites)

        val data =
            LivePagedListBuilder(dataSourceFactory, 10).setFetchExecutor(networkExecutor).build()

        val error: LiveData<String> =
            Transformations.switchMap(dataSourceFactory.source) { it.error }

        val networkState = Transformations.switchMap(dataSourceFactory.source) { it.state }

        return Result(
            data,
            error,
            networkState
        )
    }

    fun getComments(kids: List<Int>): Result<List<Item>> {
        val service = Service.create()
        val result = ArrayList<Item>()
        val data = MutableLiveData<List<Item>>()
        val error = MutableLiveData<String>()
        val networkState = MutableLiveData<NetworkState>()

        networkState.postValue(NetworkState.LOADING)
        kids.map {
            try {
                val response = service.getItem(it.toString()).execute()
                result.add(response.body()!!)
            } catch (e: Exception) {
                networkState.postValue(NetworkState.ERROR)
                error.postValue(e.toString())
            }
        }

        networkState.postValue(NetworkState.LOADED)
        data.postValue(result)

        return Result(
            data,
            error,
            networkState
        )
    }
}