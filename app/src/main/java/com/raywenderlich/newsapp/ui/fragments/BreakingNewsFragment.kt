package com.raywenderlich.newsapp.ui.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AbsListView
import android.widget.ProgressBar
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.newsapp.R
import com.raywenderlich.newsapp.Utility.Constants.Companion.QUERY_PAGE_SIZE
import com.raywenderlich.newsapp.Utility.Resource
import com.raywenderlich.newsapp.adapter.ArticleAdapter
import com.raywenderlich.newsapp.ui.NewsActivity
import com.raywenderlich.newsapp.viewModel.NewsViewModel


 const val TAG = "BreakingNewsFragment"
class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {

    private lateinit var progressBar: ProgressBar

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private lateinit var scrollListener: RecyclerView.OnScrollListener

    private lateinit var newsViewModel: NewsViewModel
    lateinit var adapter: ArticleAdapter
    lateinit var  recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_breaking_news, container, false)

        newsViewModel = (activity as NewsActivity).newsViewModel
        getLiveResponses()
        recyclerView = view.findViewById(R.id.rvBreakingNews)

        progressBar = view.findViewById(R.id.paginationProgressBar)
        recyclerViewScrollListener()
        setUpRecyclerView()
         goToArticleFragment()



        return view
    }


    private fun getLiveResponses(){
        newsViewModel.breakingNews.observe(viewLifecycleOwner){ Response ->

            when(Response){

                is Resource.Success -> {
                    hideProgress()
                    Response.data?.let { newsResponse ->

                    adapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = newsViewModel.breakingNewsPage == totalPages

                    }
                }

                is Resource.Error -> {
                    hideProgress()
                    Log.e(TAG , "error: ${Response.message}")
                }

                is Resource.Loading -> {
                    showProgress()
                }
            }



        }

    }

    private fun goToArticleFragment(){

        adapter.setOnItemClickListener { article ->

            val action = BreakingNewsFragmentDirections.actionBreakingNewsFragmentToArticleFragment(article)
            findNavController().navigate(action)


        }
    }


    private fun recyclerViewScrollListener(){
         scrollListener = object: RecyclerView.OnScrollListener(){

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true

                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount

                val isNotLoadingAndIsNotAtLastPage = !isLoading && !isLastPage
                val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
                val isNotAtBeginning = firstVisibleItemPosition > 0
                val isTotalMoreThanVisible = totalItemCount > QUERY_PAGE_SIZE
                val shouldPaginate = isNotLoadingAndIsNotAtLastPage && isAtLastItem && isNotAtBeginning
                        && isTotalMoreThanVisible

                if (shouldPaginate){

                    newsViewModel.getBreakingNews("us")
                    isScrolling = false
                }
            }
        }
    }

    private fun setUpRecyclerView(){
        adapter = ArticleAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.addOnScrollListener(this@BreakingNewsFragment.scrollListener)

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
        isLoading = true
    }


    private fun hideProgress() {
        progressBar.visibility = ProgressBar.GONE
        enableUserInteraction()
        isLoading = false
    }

}