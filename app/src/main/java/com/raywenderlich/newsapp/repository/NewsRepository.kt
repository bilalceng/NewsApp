package com.raywenderlich.newsapp.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.raywenderlich.newsapp.api.RetrofitInstance
import com.raywenderlich.newsapp.db.ArticleDatabase
import com.raywenderlich.newsapp.models.Article
import com.raywenderlich.newsapp.models.NewsResponse
import retrofit2.Response

class NewsRepository(var context: Context) {
    val db: ArticleDatabase = ArticleDatabase.getInstance(context)
    val articleDao = db.getArticleDao()


    suspend fun getBreakıngNews(country: String, page: Int): Response<NewsResponse> {
        return  RetrofitInstance.api.getBreakingNews(country,page)
    }

    suspend fun searchForNews(searchQuery: String, page: Int): Response<NewsResponse>{
        return RetrofitInstance.api.searchForNews(searchQuery, page)
    }
    suspend fun deleteArticle(article: Article){
        articleDao.deleteArticle(article)

    }

    suspend fun upsert(article: Article ): Long{
        return articleDao.upsert(article)
    }

    fun getAllArticle():LiveData<List<Article>>{
        return articleDao.getAllArticles()
    }

    fun createArticle(): Article{
        return Article()
    }
}