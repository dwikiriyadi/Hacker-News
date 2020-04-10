package io.gitlab.dwikiriyadi.hackernews.util

import io.gitlab.dwikiriyadi.hackernews.data.HackerNewsRepository
import io.gitlab.dwikiriyadi.hackernews.SharedViewModelFactory

object Injection {
    private fun providerHackerNewsRepository() = HackerNewsRepository()

    fun providerSharedViewModelFactory() = SharedViewModelFactory(providerHackerNewsRepository())
}