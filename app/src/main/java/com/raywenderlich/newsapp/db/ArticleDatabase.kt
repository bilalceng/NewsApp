package com.raywenderlich.newsapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.raywenderlich.newsapp.models.Article
import com.raywenderlich.newsapp.Utility.Converters


@Database(
    entities = [Article::class],
    version = 3
)

@TypeConverters(Converters::class)
abstract class ArticleDatabase: RoomDatabase() {

    abstract fun getArticleDao():ArticleDao


    companion object{

        private var instance: ArticleDatabase? = null
        fun getInstance(context: Context): ArticleDatabase{

            if(instance == null ){

                instance = Room.databaseBuilder(
                    context.applicationContext,
                    ArticleDatabase::class.java,
                    "articles"
                ).fallbackToDestructiveMigration()
                    .build()
            }

            return instance as ArticleDatabase

        }

    }
}