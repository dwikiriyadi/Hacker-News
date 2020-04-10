package io.gitlab.dwikiriyadi.hackernews.data.model

import androidx.lifecycle.LiveData

data class Result<T>(
    val data: LiveData<T>,
    val error: LiveData<String>,
    val networkState: LiveData<NetworkState>
)