import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PositionalDataSource
import io.gitlab.dwikiriyadi.hackernews.data.Service
import io.gitlab.dwikiriyadi.hackernews.data.model.Item
import io.gitlab.dwikiriyadi.hackernews.data.model.NetworkState
import retrofit2.Response

class HackerNewsPositionalDataSource(private val favorites: List<Int>) :
    PositionalDataSource<Item>() {

    private val _state = MutableLiveData<NetworkState>()
    private val _error = MutableLiveData<String>()

    val state: LiveData<NetworkState>
        get() = _state

    val error: LiveData<String>
        get() = _error

    private val computeCount = MutableLiveData<Int>()

    private fun loadRangeInternal(startPosition: Int, loadCount: Int): List<Item> {
        val result = ArrayList<Item>()
        val service = Service.create()
        _state.postValue(NetworkState.LOADING)

        if (favorites.isNotEmpty()) {
            favorites.map { id ->
                getStory(service, id)?.let { it -> result.add(it.apply { favorite = true }) }
            }
        }

        try {
            val responseIds: Response<List<Int>> = service.getIds().execute()
            val ids = responseIds.body() ?: emptyList()
            computeCount.postValue(ids.size)
            ids.take(loadCount).subList(startPosition, startPosition + loadCount - 1).map { id ->
                getStory(service, id)?.let { it -> result.add(it) }
            }
        } catch (e: Exception) {
            _state.postValue(NetworkState.ERROR)
            _error.postValue(e.toString())
        }

        _state.postValue(NetworkState.LOADED)
        return result
    }

    private fun getStory(service: Service, id: Int): Item? {
        val response = service.getItem(id.toString()).execute()
        return response.body()
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Item>) {
        callback.onResult(loadRangeInternal(params.startPosition, params.loadSize))
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Item>) {
        val totalCount = computeCount.value
        val position = computeInitialLoadPosition(params, totalCount!!)
        val loadSize =
            computeInitialLoadSize(params, position, totalCount)
        callback.onResult(loadRangeInternal(position, loadSize), position, totalCount)
    }

}