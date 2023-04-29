package com.raywenderlich.newsapp.ui.fragments

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
import com.raywenderlich.newsapp.R
import com.raywenderlich.newsapp.models.Article
import com.raywenderlich.newsapp.ui.NewsActivity
import com.raywenderlich.newsapp.viewModel.NewsViewModel


class ArticleFragment : Fragment(R.layout.fragment_article) {

    private lateinit var newsViewModel: NewsViewModel
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

        webView = view.findViewById(R.id.webView)
        getArticles()

        return view
    }


    private fun getArticles(){
        Log.d("yarak","${args.myArticle}")
        val article = args.myArticle
        webView.webViewClient = WebViewClient()
        webView.loadUrl(article.url)

    }




}