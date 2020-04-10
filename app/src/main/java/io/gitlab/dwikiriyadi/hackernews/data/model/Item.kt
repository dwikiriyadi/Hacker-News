package io.gitlab.dwikiriyadi.hackernews.data.model

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class Item(
    @SerializedName("id") val id: Int,
    @SerializedName("deleted") val deleted: Boolean,
    @SerializedName("type") val type: String,
    @SerializedName("by") val by: String,
    @SerializedName("time") val time: Long,
    @SerializedName("text") val text: String,
    @SerializedName("dead") val dead: Boolean,
    @SerializedName("parent") val parent: Int,
    @SerializedName("kids") val kids: List<Int>,
    @SerializedName("url") val url: String,
    @SerializedName("score") val score: Int,
    @SerializedName("title") val title: String,
    @SerializedName("descendants") val descendants: Int,
    var favorite: Boolean = false
) {
    fun ItemDate(): String {
        val date = Date(time * 1000)
        return SimpleDateFormat("dd/MM/yyyy").format(date)
    }
}