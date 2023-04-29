package com.raywenderlich.newsapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raywenderlich.newsapp.Utility.Resource
import com.raywenderlich.newsapp.api.RetrofitInstance
import com.raywenderlich.newsapp.models.Article
import com.raywenderlich.newsapp.models.NewsResponse
import com.raywenderlich.newsapp.repository.NewsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel( application:Application ): AndroidViewModel(application){

    private var articles: LiveData<List<Article>>? = null

    private val newsRepo:NewsRepository = NewsRepository(getApplication())

    var breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()

    var breakingNewsPage = 1


    var searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()

    var searchNewsPage = 1

    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(countryCode: String): Job {

        val job =  viewModelScope.launch {
            breakingNews.postValue(Resource.Loading())
            val response = RetrofitInstance.api.getBreakingNews(countryCode,breakingNewsPage)

            breakingNews.postValue(handleBreakingNewsResponse(response))
        }

        return job
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
            if(response.isSuccessful){
                response.body()?.let{ result ->
                    return Resource.Success(result)
                }
            }

                return Resource.Error(response.message())

    }


    fun getSearchNews(searchQuery: String): Job {

        val job =  viewModelScope.launch {
            searchNews.postValue(Resource.Loading())
            val response = newsRepo.searchForNews(searchQuery,searchNewsPage)

            searchNews.postValue(handleSearchNewsResponse(response))
        }

        return job
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if(response.isSuccessful){
            response.body()?.let{ result ->
                return Resource.Success(result)
            }
        }

        return Resource.Error(response.message())

    }




}