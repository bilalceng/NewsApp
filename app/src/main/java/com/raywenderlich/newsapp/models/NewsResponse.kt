package com.raywenderlich.newsapp.models

import com.raywenderlich.newsapp.models.Article

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)