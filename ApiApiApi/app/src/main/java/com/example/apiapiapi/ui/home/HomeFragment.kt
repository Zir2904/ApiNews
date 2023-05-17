package com.example.apiapiapi.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.apiapiapi.klase.Article
import com.example.apiapiapi.Constants
import com.example.apiapiapi.R
import com.example.apiapiapi.databinding.FragmentHomeBinding
import com.example.apiapiapi.ui.dashboard.ArticleAdapter
import com.example.apiapiapi.ui.dashboard.SortHelper
import com.squareup.picasso.Picasso

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var articleAdapter: ArticleAdapter
    private var displayedArticlesCount = 0  // Variable to store the number of displayed articles

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    var totResults: Int = 0 // Variable to store total number of news
    private lateinit var searchImage: ImageView
    private lateinit var sortHelper: SortHelper
    private lateinit var articles: List<Article>    // list of articles
    private lateinit var scrollView: NestedScrollView
    private var currentSortOptionIndex = 0  // Variable storing the current index of sorting option
    private var userInput = ""  // Variable to store users keyword
    var firstDisplay: Int = 0   // Variable for checking if total results popup showed already

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        articleAdapter = ArticleAdapter()
        val root: View = binding.root
        scrollView = view.findViewById(R.id.nestedScrollView)
        sortHelper = SortHelper()
        binding.recyclerView.adapter = articleAdapter
        userInput = ""
        val refreshButton = view.findViewById<Button>(R.id.refreshButton)
        val keywordEditText = root.findViewById<EditText>(R.id.keywordEditText)
        val sortButton = view.findViewById<Button>(R.id.sortButtonHome)

        // Put a search button on users keyboard and when its pressed if
        // nothing is typed in show the message and icon
        // else set the page number to 1 and popup counter to 0
        // and call the function to fetch articles with keyword
        keywordEditText.inputType = InputType.TYPE_CLASS_TEXT
        keywordEditText.imeOptions = EditorInfo.IME_ACTION_SEARCH
        keywordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                userInput = keywordEditText.text.toString()

                val linearLayout = binding.linear
                linearLayout.removeAllViews()

                if(userInput.isNullOrEmpty()) {
                    sortButton.visibility = View.GONE
                    refreshButton.visibility = View.GONE
                    val something = TextView(requireContext())
                    something.text = "At least type in something :)"
                    linearLayout.addView(something)

                    searchImage = view.findViewById(R.id.searchImageHome)
                    searchImage.setImageResource(R.drawable.baseline_emoji_emotions_24)
                    searchImage.visibility = View.VISIBLE
                } else {
                    firstDisplay = 0
                    viewModel.pageNum = 1
                    viewModel.fetchNewsArticles(userInput)
                }
                displayedArticlesCount = 0

                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        viewModel.news.observe(viewLifecycleOwner, Observer { news ->

            // set the variables to fetched data, and call the popup function
            if (news != null) {
                articles = news.articles
            }
            if (news != null) {
                totResults = news.totalResults
            }

            if(firstDisplay == 0){
                context?.let { sortHelper.showAmount(it,totResults, firstDisplay, Constants.maxEverythingPerPage)
                    firstDisplay = 1}
            }

            // If number of articles is more than zero set the original order, show
            // refresh and sort buttons and set their index and color to default
            // remove the search icon in view and call function to display articles
            // If there are no articles remove the buttons and add the text no results,
            // and show the X icon in view
            if(totResults != 0) {
                sortHelper.setOriginalOrder(articles)
                sortButton.visibility = View.VISIBLE
                refreshButton.visibility = View.VISIBLE
                sortHelper.setSortOrder(SortHelper.SortOrder.NORMAL)
                sortHelper.setSortBy(SortHelper.SortBy.DATE)

                currentSortOptionIndex = 0
                sortButton.setBackgroundColor(Constants.sortColors[currentSortOptionIndex])
                sortHelper.changeButtonText(sortButton)
                searchImage = view.findViewById(R.id.searchImageHome)
                searchImage.visibility = View.GONE
                displayArticles(articles)
            }
            else {
                val noResultsTextView = TextView(requireContext())
                noResultsTextView.text = "No results found."

                val linearLayout = binding.linear
                linearLayout.addView(noResultsTextView)
                sortButton.visibility = View.GONE
                refreshButton.visibility = View.GONE

                searchImage = view.findViewById(R.id.searchImageHome)
                searchImage.setImageResource(R.drawable.baseline_close_24)
                searchImage.visibility = View.VISIBLE
            }
        })

        // Add a listener to refresh button that resets the pageNumber, resets the view
        // and fetches articles again with the same keyword
        refreshButton.setOnClickListener {
            viewModel.pageNum = 1
            displayedArticlesCount = 0
            firstDisplay = 0
            val linearLayout = binding.linear
            linearLayout.removeAllViews()
            context?.let { it1 -> sortHelper.showPopupMessage(it1,2) }
            viewModel.fetchNewsArticles(userInput)
        }

        // Add onClick listener to sort the articles in asc, desc and normal order,
        // change the text of the button, remove all articles and display them again
        sortButton.setOnClickListener {
            sortHelper.toggleSortOrder()
            context?.let { it1 -> sortHelper.showPopupMessage(it1, 1) }
            sortHelper.changeButtonText(sortButton)
            var sortedArticles = sortHelper.sortArticles(articles)
            articles = sortedArticles
            displayedArticlesCount = 0
            val linearLayout = binding.linear
            linearLayout.removeAllViews()
            scrollView.smoothScrollTo(0, 0)
            displayArticles(sortedArticles)
        }

        // Set OnLongClick listener to sortButton -> it changes
        // the sorting algorithm (date, author, title) and sets the sort option to normal
        // and change the color of the button, index and text, also call function to display
        // the message "Changed the sorthing algorithm to ____"
        sortButton.setOnLongClickListener {
            sortButton.setBackgroundColor(Constants.sortColors[currentSortOptionIndex])
            currentSortOptionIndex = (currentSortOptionIndex + 1) % Constants.sortColors.size
            sortHelper.toggleSortBy()
            sortHelper.setSortOrder(SortHelper.SortOrder.NORMAL)
            context?.let { it1 -> sortHelper.showPopupMessage(it1, 0) }
            sortHelper.changeButtonText(sortButton)
            displayedArticlesCount = 0
            true
        }

        // Add onclick listener to loadmore button
        val loadMoreButton = binding.loadMoreButton
        loadMoreButton.setOnClickListener { onLoadMoreButtonClick(articles) }
    }

    private fun displayArticles(articles: List<Article>) {
        val linearLayout = binding.linear

        if(sortHelper.getOriginalOrder().isNullOrEmpty()){
            // If not set already, set the firstly fetched order of articles as original order
            sortHelper.setOriginalOrder(articles)
        }

        if (linearLayout.childCount > 1) {
            // Remove the "Load More" button before displaying more articles
            linearLayout.removeViewAt(linearLayout.childCount - 1)
        }

        // Calculate the end index based on the number of displayed articles to get only the not
        // displayed but already fetched articles
        val endIndex = minOf(displayedArticlesCount + Constants.maxArticlesToShow, articles.size)
        val articlesToShow = articles.subList(displayedArticlesCount, endIndex)

        // Add Views dynamically for each article
        for (article in articlesToShow) {
            displayedArticlesCount += 1
            val articleView = LayoutInflater.from(requireContext()).inflate(R.layout.item_article, linearLayout, false)

            // Set articles title and source
            val titleTextView = articleView.findViewById<TextView>(R.id.titleTextView)
            titleTextView.text = article.title

            val sourceTextView = articleView.findViewById<TextView>(R.id.sourceTextView)
            sourceTextView.text = article.source.name

            // If article has author set the text to it, if not set it to "Unknown"
            val authorTextView = articleView.findViewById<TextView>(R.id.authorTextView)
            val author = article.author
            if (author.isNullOrEmpty() || author == "") {
                authorTextView.text = "Author: Unknown"
            } else {
                authorTextView.text = "Author: $author"
            }

            // If article has date set the text to it, if not set it to "Unknown"
            val dateTextView = articleView.findViewById<TextView>(R.id.dateTextView)
            val date = article.publishedAt
            if (date.isNullOrEmpty() || date == "") {
                dateTextView.text = "Date: Unknown"
            } else {
                dateTextView.text = "Date: $date"
            }

            // If article has description set the description text to it
            if(article.description.isNullOrEmpty() || article.description.equals("")) {

            } else {
                val descriptionTextView =
                    articleView.findViewById<TextView>(R.id.descriptionTextView)
                descriptionTextView.text = article.description
            }

            // Make an URL to the article blue, set its text and functionality
            val linkTextView = articleView.findViewById<TextView>(R.id.linkTextView)
            linkTextView.apply {
                text = SpannableString("Click here to visit the website").apply {
                    setSpan(object : ClickableSpan() {
                        override fun onClick(widget: View) {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
                            startActivity(intent)
                        }
                    }, 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                movementMethod = LinkMovementMethod.getInstance()
            }

            // If article has an url image use Picaso library to display it
            if(article.urlToImage.isNullOrEmpty() || article.urlToImage.equals("")) {

            } else {
                val imageView = articleView.findViewById<ImageView>(R.id.imageView)
                Picasso.get().load(article.urlToImage).into(imageView)
            }
            linearLayout.addView(articleView)
        }

        // Add the button at the bottom of the linear layout if there is more articles
        // set text to "Load more" and add onclick listener
        // else set the button with "No more articles" text and without listener
        // Less than 6 because my APIkey doesn't allow to get more news than that
        if (endIndex < totResults && (viewModel.pageNum+1 < 6)) {
            val loadMoreButton = Button(requireContext())
            loadMoreButton.text = "Load More"

            loadMoreButton.setOnClickListener {
                onLoadMoreButtonClick(this.articles)
            }
            linearLayout.addView(loadMoreButton)
        }
        else{
            val loadMoreButton = Button(requireContext())
            loadMoreButton.text = "No more articles"
            linearLayout.addView(loadMoreButton)
        }
    }

    // Call the displayArticles function which will display more already fetched articles (10 by 10 until end of the page 100)
    // but if we displayed all articles on the page (maxEverythingPerPage -> 100) increase the page number and fetch new articles
    // before calling the function
    private fun onLoadMoreButtonClick(articles: List<Article>) {
        if(displayedArticlesCount %Constants.maxEverythingPerPage == 0 && displayedArticlesCount  < totResults && ((viewModel.pageNum +1)<6)) {
            viewModel.pageNum++
            viewModel.fetchNewsArticles(userInput)
        }
        displayArticles(articles)

    }

    override fun onDestroy() {
        super.onDestroy()
        displayedArticlesCount = 0
        viewModel.pageNum = 0
        viewModel.reset()
    }

}