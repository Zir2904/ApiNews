package com.example.apiapiapi.ui.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.apiapiapi.klase.Article
import com.example.apiapiapi.Constants
import com.example.apiapiapi.News
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class DashboardViewModel : ViewModel() {

    var _news = MutableLiveData<News?>()
    var news: MutableLiveData<News?> = _news
    var pageNum: Int = 1
    var allArticles: MutableList<Article> = mutableListOf()

    fun fetchNewsArticles(userInput: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val apiKey = Constants.apiKey
            val endpoint = Constants.topHeadlinesEndpoint
            val url = URL("$endpoint?q=$userInput&page=$pageNum&apiKey=$apiKey")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("User-Agent", "Your User Agent")
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Host", "newsapi.org")
            connection.setRequestProperty("Accept", "*/*")
            connection.setRequestProperty("Connection", "keep-alive")
            println("res kod je " + connection.responseCode)
            println("user input je "+userInput.isNullOrEmpty() +" aaa")

            try {
                connection.connect()

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val inputSystem = connection.inputStream
                    val inputStreamReader = InputStreamReader(inputSystem, "UTF-8")
                    val newNews: News = Gson().fromJson(inputStreamReader, News::class.java)
                    println("velicinaaa jest "+newNews.totalResults)
                    if(pageNum > 1) {
                        println("Dosao sam ovdje")
                        val currentNews = _news.value
                        val combinedNews = News(
                            (currentNews?.articles ?: emptyList<Article>()) + newNews.articles,
                            newNews.status,
                            newNews.totalResults
                        )
                        _news.postValue(combinedNews)
                    } else {
                        println("Dosao sam tu")
                        _news.postValue(newNews)
                    }

                } else {
                    val errorStream = connection.errorStream
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                connection.disconnect()
            }
        }
    }

    fun reset() {
        _news.value = null
        pageNum = 1
    }

}
