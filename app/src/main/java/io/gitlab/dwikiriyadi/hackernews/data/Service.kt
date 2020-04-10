package io.gitlab.dwikiriyadi.hackernews.data

import io.gitlab.dwikiriyadi.hackernews.data.model.Item
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface Service {

    @GET("topstories.json?print=pretty")
    fun getIds(): Call<List<Int>>

    @GET("item/{item}.json?print=pretty")
    fun getItem(@Path("item") item: String): Call<Item>

    companion object {
        private const val baseURL = "https://hacker-news.firebaseio.com/v0/"

        fun create(): Service {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseURL).build().create(Service::class.java)
        }
    }
}