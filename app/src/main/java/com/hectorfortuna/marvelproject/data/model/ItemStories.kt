package com.hectorfortuna.marvelproject.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ItemStories(
    @SerializedName("resourceURI")
    val resourceURI: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("returned")
    val returned: Int
): Serializable
