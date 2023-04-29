package com.raywenderlich.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.newsapp.R
import com.raywenderlich.newsapp.Utility.Constants
import com.raywenderlich.newsapp.Utility.Resource
import com.raywenderlich.newsapp.adapter.ArticleAdapter
import com.raywenderlich.newsapp.ui.NewsActivity
import com.raywenderlich.newsapp.viewModel.NewsViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {

    private lateinit var newsViewModel: NewsViewModel
    private lateinit var progressBar: ProgressBar
    lateinit var adapter: ArticleAdapter
    lateinit var  recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search_news, container, false)

        setHasOptionsMenu(true)

        newsViewModel = (activity as NewsActivity).newsViewModel
        getLiveResponses()
        recyclerView = view.findViewById(R.id.rvSearchNews)
        setUpRecyclerView()
        progressBar = view.findViewById(R.id.paginationProgressBar)

        goToArticleFragment()
        return view
    }



    private fun getLiveResponses(){
        newsViewModel.searchNews.observe(viewLifecycleOwner){ Response ->

            when(Response){

                is Resource.Success -> {
                    hideProgress()
                    Response.data?.let { newsResponse ->

                        adapter.differ.submitList(newsResponse.articles)

                    }
                }

                is Resource.Error -> {
                    hideProgress()
                    Log.e(TAG , "error: ${Response.message}")
                }

                is Resource.Loading ->{
                    showProgress()
                }
            }



        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)

        val searchItem = menu.findItem(R.id.search)
        val searchView = searchItem.actionView as SearchView

        searchView.queryHint = "search news ..."

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                var job: Job? = null
                job?.cancel()

                job = MainScope().launch {
                    delay(Constants.DELAY)
                    newText?.let {
                        newsViewModel.getSearchNews(newText)
                    }

                }

                return true
            }

        })
    }

    private fun goToArticleFragment(){

        adapter.setOnItemClickListener { article ->
            val bundle = Bundle()
            bundle.putSerializable("bilal",article)

            findNavController().navigate(R.id.action_searchNewsFragment_to_articleFragment,
                bundle)

        }
    }


    private fun setUpRecyclerView(){
        adapter = ArticleAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

    }



    private fun disableUserInteraction() {
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)
    }


    private fun enableUserInteraction() {
        activity?.window?.clearFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }


    private fun showProgress() {
        progressBar.visibility = ProgressBar.VISIBLE
        disableUserInteraction()
    }


    private fun hideProgress() {
        progressBar.visibility = ProgressBar.GONE
        enableUserInteraction()
    }


}