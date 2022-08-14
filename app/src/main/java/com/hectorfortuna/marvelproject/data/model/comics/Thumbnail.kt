package com.hectorfortuna.marvelproject.data.model.comics

data class Thumbnail(
    val extension: String,
    val path: String,
    val width: Int,
    val height: Int
){
    val aspectRatio: Float get() = 1f
}