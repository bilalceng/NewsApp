package com.raywenderlich.newsapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.raywenderlich.newsapp.Article


@Database(
    entities = [Article::class],
    version = 1
)
abstract class ArticleDatabase: RoomDatabase() {

    abstract fun getArticleDao():ArticleDao

    companion object{
        @Volatile
        private var instance:ArticleDatabase? = null
        private val LOCK = Any()
    }
}