package com.raywenderlich.newsapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.newsapp.R
import com.raywenderlich.newsapp.adapter.ArticleAdapter
import com.raywenderlich.newsapp.ui.NewsActivity
import com.raywenderlich.newsapp.viewModel.NewsViewModel

class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {


    lateinit var adapter: ArticleAdapter
    lateinit var  recyclerView: RecyclerView

    private  lateinit var  newsViewModel: NewsViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_saved_news, container, false)
        newsViewModel = (activity as NewsActivity).newsViewModel
        recyclerView = view.findViewById(R.id.rvSavedNews)

        setUpRecyclerView()
        goToArticleFragment()
        return view
    }

    private fun setUpRecyclerView(){
        adapter = ArticleAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

    }

    private fun goToArticleFragment(){

        adapter.setOnItemClickListener { article ->
            val bundle = Bundle()
            bundle.putSerializable("bilal",article)

            findNavController().navigate(R.id.action_savedNewsFragment_to_articleFragment,
                bundle)

        }
    }


}