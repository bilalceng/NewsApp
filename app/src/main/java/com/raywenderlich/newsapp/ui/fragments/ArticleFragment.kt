package com.raywenderlich.newsapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import androidx.navigation.navArgument
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.raywenderlich.newsapp.R
import com.raywenderlich.newsapp.models.Article
import com.raywenderlich.newsapp.ui.NewsActivity
import com.raywenderlich.newsapp.viewModel.NewsViewModel


class ArticleFragment : Fragment(R.layout.fragment_article) {

    private lateinit var newsViewModel: NewsViewModel
    private lateinit var shareButton: FloatingActionButton
    private lateinit var fabButton: FloatingActionButton
    private lateinit var webView: WebView
    private val args: ArticleFragmentArgs by navArgs<ArticleFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.d("yarak","${args.myArticle}")
        Log.d("yarak","burdayım2")

        newsViewModel = (activity as NewsActivity).newsViewModel
       val view =  inflater.inflate(R.layout.fragment_article, container, false)
        shareButton = view.findViewById(R.id.ShareFab)
        fabButton = view.findViewById(R.id.fab)

        webView = view.findViewById(R.id.webView)
        getArticles()



        return view
    }


    private fun getArticles(){
        Log.d("yarak","${args.myArticle}")
        val article = args.myArticle
        webView.webViewClient = WebViewClient()
        article?.let { it.url }?.let { webView.loadUrl(it) }

        shareButton.setOnClickListener {
            shareNews(article)
        }

        fabButton.setOnClickListener {
            saveCurrentArticle(article)
            Snackbar.make(it,"article saved successfully.", Snackbar.LENGTH_SHORT).show()

        }


    }

    private fun saveCurrentArticle(article: Article){
        newsViewModel.upsert(article)
    }

    fun shareNews(article: Article){

        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND

        sendIntent.putExtra(Intent.EXTRA_TEXT,
            "Check out ${article.title} at:\n${article.url}")
        sendIntent.putExtra(Intent.EXTRA_SUBJECT,
            "Sharing ${article.source}")

        sendIntent.type = "text/plain"

        startActivity(sendIntent)

    }



}