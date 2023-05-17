package com.example.apiapiapi.ui.notifications

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.apiapiapi.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        var textView: TextView = binding.descriptionTextView
            textView.text = "This application is for easy access to news articles. To find want you want you first need " +
                    "to type in a keyword and the app will show you all the relevant articles. The displayed articles can " +
                    "be sorted by date published, author and title in normal, ascending and descending order. Sorting is done by " +
                    "clicking the sort button and by holding the button you can change the option to sort by. See below for examples."


        val dateColor = "#8C319C"
        val authorColor = "#9B23D0"

        textView = binding.textView
        textView.text = "User typed the keyword bitcoin"

        var buttonView: Button = binding.button1
        textView = binding.button1Text
        textView.text = "Sort articles by date in ascending order"
        buttonView.text = "D SORT A"
        buttonView.setBackgroundColor(Color.MAGENTA)

        textView = binding.button2Text
        textView.text = "Sort articles by date in descending order"
        buttonView = binding.button2
        buttonView.text = "D SORT D"
        buttonView.setBackgroundColor(Color.MAGENTA)

        textView = binding.button3Text
        textView.text = "Sort articles by title in ascending order"
        buttonView = binding.button3
        buttonView.text = "T SORT A"
        buttonView.setBackgroundColor(Color.parseColor(authorColor))

        textView = binding.button4Text
        textView.text = "Sort articles by author in normal order"
        buttonView = binding.button4
        buttonView.text = "A SORT N"
        buttonView.setBackgroundColor(Color.parseColor(dateColor))

        textView = binding.button5Text
        textView.text = "Button at the end of displayed articles if there are more"
        buttonView = binding.button5
        buttonView.setTextColor(Color.WHITE)
        buttonView.text = "Load more"
        buttonView.setBackgroundColor(Color.GRAY)

        textView = binding.button6Text
        textView.text = "Shown if there are no more articles to display"
        buttonView = binding.button6
        buttonView.text = "No more articles"
        buttonView.setTextColor(Color.WHITE)
        buttonView.setBackgroundColor(Color.GRAY)

        textView = binding.button7Text
        textView.text = "No results found."

        textView = binding.button77Text
        textView.text = "Message displayed if no articles with the keyword were found"

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}