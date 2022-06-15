package com.hectorfortuna.marvelproject.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Series(
    @SerializedName("available")
    val available: Int,
    @SerializedName("items")
    val items: List<ItemSeries>,
    @SerializedName("returned")
    val returned: Int
) : Serializable
