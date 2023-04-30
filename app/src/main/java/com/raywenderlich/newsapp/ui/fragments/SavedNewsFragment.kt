package com.raywenderlich.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
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
        updateSavedList()
        swipeToDelete()
        return view
    }

    private fun setUpRecyclerView(){
        adapter = ArticleAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

    }

    private fun goToArticleFragment(){

        adapter.setOnItemClickListener { article ->
            val action = SavedNewsFragmentDirections.actionSavedNewsFragmentToArticleFragment(article)
            findNavController().navigate(action)

        }
    }

    private fun updateSavedList(){
        newsViewModel.getSavedNews().observe(viewLifecycleOwner){
            adapter.differ.submitList(it)
        }
    }

    private fun swipeToDelete(){
        val itemTouchHelperCallback = object: ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {

                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition


                if (fromPosition < toPosition) {

                    for (i in fromPosition until toPosition) {
                        adapter.notifyItemMoved(i, i + 1)
                    }
                } else {

                    for (i in fromPosition downTo toPosition + 1) {
                        adapter.notifyItemMoved(i, i - 1)
                    }


                }


                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = adapter.differ.currentList[position]
                newsViewModel.deleteArticle(article)

                view?.let {
                    Snackbar.make(it,"successfully deleted article", Snackbar.LENGTH_LONG).apply {
                            setAction("undo"){
                                newsViewModel.upsert(article)
                            }
                        show()
                    }
                }
            }

        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(recyclerView)
        }
    }


}