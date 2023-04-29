package com.raywenderlich.newsapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.raywenderlich.newsapp.R
import com.raywenderlich.newsapp.ui.NewsActivity
import com.raywenderlich.newsapp.viewModel.NewsViewModel


class ArticleFragment : Fragment(R.layout.fragment_article) {

    private lateinit var newsViewModel: NewsViewModel
    private lateinit var webView: WebView
    val args: ArticleFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        newsViewModel = (activity as NewsActivity).newsViewModel
       val view =  inflater.inflate(R.layout.fragment_article, container, false)

        webView = view.findViewById(R.id.webView)
        getArticles()

        return view
    }


    private fun getArticles(){
        val article = args.article
        webView.webViewClient = WebViewClient()
        webView.loadUrl(article.url)

    }




}