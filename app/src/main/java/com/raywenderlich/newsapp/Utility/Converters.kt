package com.raywenderlich.newsapp.Utility

import androidx.room.TypeConverter
import com.raywenderlich.newsapp.models.Source


class Converters {

    @TypeConverter
    fun fromSourceToString(source: Source): String{
        return source.name
    }

    @TypeConverter
    fun fromStringToSource(name: String): Source {
        return Source(name, name)
    }



}