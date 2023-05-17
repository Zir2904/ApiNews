package com.example.apiapiapi.ui.dashboard

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.apiapiapi.klase.Article
import com.example.apiapiapi.R

class SortHelper {

    enum class SortOrder(s: String) {
        ASCENDING("ascending"),
        DESCENDING("descending"),
        NORMAL("normal")
    }

    enum class SortBy(s: String) {
        AUTHOR("author"),
        TITLE("title"),
        DATE("date")
    }

    private var sortOrder = SortOrder.NORMAL
    private var sortBy = SortBy.DATE
    private var originalArticles: List<Article> = emptyList()

    fun getSortOrder(): SortOrder {
        return sortOrder
    }

    fun getSortBy(): SortBy {
        return sortBy
    }

    fun getNextSortOrder(currentSortOrder: SortOrder) : SortOrder {
        return when (sortOrder) {
            SortOrder.ASCENDING -> SortOrder.DESCENDING
            SortOrder.DESCENDING -> SortOrder.NORMAL
            SortOrder.NORMAL -> SortOrder.ASCENDING
        }
    }

    fun toggleSortOrder() {
        sortOrder = when (sortOrder) {
            SortOrder.ASCENDING -> SortOrder.DESCENDING
            SortOrder.DESCENDING -> SortOrder.NORMAL
            SortOrder.NORMAL -> SortOrder.ASCENDING
        }
    }

    fun toggleSortBy() {
        sortBy = when (sortBy) {
            SortBy.AUTHOR -> SortBy.TITLE
            SortBy.TITLE -> SortBy.DATE
            SortBy.DATE -> SortBy.AUTHOR
        }
    }

    fun setSortOrder(order: SortOrder) {
        sortOrder = order
    }

    fun setSortBy(order: SortBy) {
        sortBy = order
    }

    fun setOriginalOrder(articles: List<Article>) {
            originalArticles = articles.toList()
    }

    fun getOriginalOrder(): List<Article> {
        return originalArticles
    }

    fun sortArticles(articles: List<Article>): List<Article> {

        if (originalArticles.isEmpty()) {
            originalArticles = articles.toList()
        }
        return when (sortOrder) {
            SortOrder.ASCENDING -> sortAscending(articles)
            SortOrder.DESCENDING -> sortDescending(articles)
            SortOrder.NORMAL -> originalArticles
        }
    }

    private fun sortDescending(articles: List<Article>): List<Article> {
        return when (sortBy) {
            SortBy.AUTHOR -> articles.sortedByDescending { if (it.author.isNullOrEmpty() || it.author.equals(""))  "Unknown author" else it.author }
            SortBy.TITLE -> articles.sortedByDescending { it.title }
            SortBy.DATE -> articles.sortedByDescending { if (it.publishedAt.isNullOrEmpty() || it.publishedAt.equals("")) "Unknown date" else it.publishedAt }
        }
    }

    private fun sortAscending(articles: List<Article>): List<Article> {
        return when (sortBy) {
            SortBy.AUTHOR -> articles.sortedBy { if (it.author.isNullOrEmpty() || it.author.equals(""))  "Unknown author" else it.author }
            SortBy.TITLE -> articles.sortedBy { it.title }
            SortBy.DATE -> articles.sortedBy { if (it.publishedAt.isNullOrEmpty() || it.publishedAt.equals("")) "Unknown date" else it.publishedAt }
        }
    }

    fun showPopupMessage(context: Context, i: Int) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.toast_layout, null)
        var message = "Error"
        if(i == 0) {
             message = "Article algorithm changed to "+ getSortBy()
        } else if(i==1){
            message = "Articles sorted by " + getSortBy() + " in " + getSortOrder() + " order"
        } else if(i==2) {
            message = "News refreshed"
        }
        val toast = Toast(context)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout

        val toastText = layout.findViewById<TextView>(R.id.toast_text)
        toastText.text = message

        toast.setGravity(Gravity.TOP, 0, 0)
        toast.show()
    }

    fun changeButtonText(sortButton: Button?) {
        if (sortButton != null) {
            sortButton.text = getSortBy().name.first() + " SORT " + getNextSortOrder(getSortOrder()).name.first()
        }
    }

    fun showAmount(context: Context, totResults: Int, firstDisplay: Int, size: Int) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.toast_layout, null)
        var message = "Error"
        if(firstDisplay==0) {
            message = totResults.toString() + " articles found."
        } else if(firstDisplay==1) {
            message = "Next page shown."
        }

        val toast = Toast(context)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout

        val toastText = layout.findViewById<TextView>(R.id.toast_text)
        toastText.text = message

        toast.setGravity(Gravity.TOP, 0, 0)
        toast.show()
    }

}
