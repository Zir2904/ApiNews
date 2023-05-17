package com.example.apiapiapi.klase

import android.os.Parcel
import android.os.Parcelable
import com.example.apiapiapi.klase.Source

data class Article(
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String
)