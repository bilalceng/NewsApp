package com.raywenderlich.newsapp.ui.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.newsapp.R
import com.raywenderlich.newsapp.Utility.Resource
import com.raywenderlich.newsapp.adapter.ArticleAdapter
import com.raywenderlich.newsapp.ui.NewsActivity
import com.raywenderlich.newsapp.viewModel.NewsViewModel


 const val TAG = "BreakingNewsFragment"
class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {

    private lateinit var progressBar: ProgressBar

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
        setUpRecyclerView()
        progressBar = view.findViewById(R.id.paginationProgressBar)

        return view
    }


    private fun getLiveResponses(){
        newsViewModel.breakingNews.observe(viewLifecycleOwner){ Response ->

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