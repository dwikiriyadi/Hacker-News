package io.gitlab.dwikiriyadi.hackernews

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import io.gitlab.dwikiriyadi.hackernews.data.HackerNewsRepository
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import io.gitlab.dwikiriyadi.hackernews.data.model.Item
import io.gitlab.dwikiriyadi.hackernews.data.model.Result

class SharedViewModel(private val repository: HackerNewsRepository) : ViewModel() {

    private val favoriteStory = ArrayList<Int>()
    private val favoriteLiveData = MutableLiveData<List<Int>>()
    private val result: LiveData<Result<PagedList<Item>>> =
        Transformations.map(favoriteLiveData) { repository.getTopStories(it) }

    val stories = Transformations.switchMap(result) { it.data }
    val error = Transformations.switchMap(result) { it.error }
    val networkState = Transformations.switchMap(result) { it.networkState }

    val story = MutableLiveData<Item>()
    private val detailResult = Transformations.map(story) { repository.getComments(it.kids) }

    val comments = Transformations.switchMap(detailResult) { it.data }
    val detailError = Transformations.switchMap(detailResult) { it.error }
    val detailNetworkState = Transformations.switchMap(detailResult) { it.networkState }

    fun addToFavorite(ids: Int) {
        favoriteStory.add(ids)
        favoriteLiveData.postValue(favoriteStory)
    }

    fun getStory(story: Item) {
        this.story.postValue(story)
    }

    fun getData() {
        if (favoriteStory.isEmpty())
            this.favoriteLiveData.postValue(ArrayList())
        else
            this.favoriteLiveData.postValue(favoriteStory)
    }
}