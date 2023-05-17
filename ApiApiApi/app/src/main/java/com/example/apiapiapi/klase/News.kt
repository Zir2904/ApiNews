package com.example.apiapiapi

import com.example.apiapiapi.klase.Article

data class News(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)