package com.raywenderlich.newsapp.models

import com.raywenderlich.newsapp.models.Article

data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)