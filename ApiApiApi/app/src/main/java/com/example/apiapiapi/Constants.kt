package com.example.apiapiapi

import android.graphics.Color

class Constants {
    companion object {
        const val apiKey = "21c9de366cfe42e0a2d4d6ff418d44be"
      //  const val apiKey = "aa70aebd45264a80a591079b25a97ea6"
      //  const val apiKey = "9d4208530c614ffeadb8ec430e2d7254"
        const val everythingEndpoint = "https://newsapi.org/v2/everything"
        const val topHeadlinesEndpoint = "https://newsapi.org/v2/top-headlines"
        const val maxEverythingPerPage = 100
        const val maxTopHeadlinesPerPage = 20
        const val dateColor = "#8C319C"
        const val authorColor = "#9B23D0"
        val sortColors = listOf(Color.MAGENTA, Color.parseColor(dateColor), Color.parseColor(authorColor))
        const val maxArticlesToShow = 10 // Adjust the maximum number of articles to show
    }
}
