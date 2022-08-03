package com.hectorfortuna.marvelproject.data.db.converters

import android.net.Uri
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hectorfortuna.marvelproject.data.model.*
import java.lang.reflect.Type
import java.util.*

class Converters {
    @TypeConverter
    fun fromThumbnail(thumbnail: Thumbnail): String =
        Gson().toJson(thumbnail)

    @TypeConverter
    fun toThumbnail(string: String): Thumbnail =
        Gson().fromJson(string, Thumbnail::class.java)

    @TypeConverter
    fun fromListUrls(listUrls: List<Urls>): String =
        Gson().toJson(listUrls)

    @TypeConverter
    fun toListUrls(string: String?): List<Urls?>? {
        if (string == null) {
            return Collections.emptyList()
        }
        val listType: Type = object : TypeToken<List<Urls?>?>() {}.type
        return Gson().fromJson<List<Urls?>>(string, listType)
    }

    @TypeConverter
    fun fromUri(uri: Uri): String =
        uri.toString()

    @TypeConverter
    fun toUri(string: String): Uri =
        Uri.parse(string)

    @TypeConverter
    fun fromListFavourites(listFavourites: List<Results?>): String =
        Gson().toJson(listFavourites)

    @TypeConverter
    fun toListFavourites(string: String?): List<Results?>? {
        if (string == null) {
            return Collections.emptyList()
        }
        val listType: Type = object : TypeToken<List<Results?>?>() {}.type
        return Gson().fromJson<List<Results?>>(string, listType)
    }

    @TypeConverter
    fun fromComics(comics: Comics): String =
        Gson().toJson(comics)

    @TypeConverter
    fun toComics(string: String): Comics =
        Gson().fromJson(string, Comics::class.java)

    @TypeConverter
    fun fromSeries(series: Series): String =
        Gson().toJson(series)

    @TypeConverter
    fun toSeries(string: String): Series =
        Gson().fromJson(string, Series::class.java)

    @TypeConverter
    fun fromStories(stories: Stories): String =
        Gson().toJson(stories)

    @TypeConverter
    fun toStories(string:String): Stories =
        Gson().fromJson(string, Stories::class.java)

    @TypeConverter
    fun toEvents(events: Events): String =
        Gson().toJson(events)

    @TypeConverter
    fun fromEvents(string: String): Events =
        Gson().fromJson(string, Events::class.java)


}
