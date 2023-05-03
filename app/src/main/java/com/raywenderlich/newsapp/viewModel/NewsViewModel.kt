package com.raywenderlich.newsapp.viewModel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raywenderlich.newsapp.Utility.Resource
import com.raywenderlich.newsapp.models.Article
import com.raywenderlich.newsapp.models.NewsResponse
import com.raywenderlich.newsapp.repository.NewsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel( application:Application ): AndroidViewModel(application){

    private var articles: LiveData<List<Article>>? = null

    private val newsRepo:NewsRepository = NewsRepository(getApplication())

    var breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()



    var breakingNewsPage = 1
    var breakingNewsResponse: NewsResponse? = null

    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null


    var searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()




    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(countryCode: String): Job {

        val job =  viewModelScope.launch {
            breakingNews.postValue(Resource.Loading())
                try {

                    if(isInternetAccessible(getApplication())){
                        Log.d("BreakMe"," waooowwwww i am here")
                        val response = newsRepo.getBreakingNews(countryCode,breakingNewsPage)
                        breakingNews.postValue(handleBreakingNewsResponse(response))
                    }else{
                        Toast.makeText(getApplication(), "internet is not accessible", Toast.LENGTH_SHORT).show()
                    }

                }catch (t:Throwable){

                    when(t){
                        is IOException ->  Toast.makeText(getApplication(), "internet is not accessible", Toast.LENGTH_SHORT).show()
                    }

                }

        }

        return job
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
            if(response.isSuccessful){
                response.body()?.let{ result ->
                    breakingNewsPage++

                    if(breakingNewsResponse == null){
                        breakingNewsResponse = result
                    }else{
                        var oldArticles = breakingNewsResponse?.articles
                        var newArticles = result.articles
                        oldArticles?.addAll(newArticles)



                    }
                    return Resource.Success(result)


                }
            }

                return Resource.Error(response.message())

    }



    fun getSearchNews(searchQuery: String): Job {

        val job = viewModelScope.launch {
            searchNews.postValue(Resource.Loading())
            try {

                if (isInternetAccessible(getApplication())) {
                    Log.d("BreakMe", " waooowwwww i am here")
                    val response = newsRepo.searchForNews(searchQuery, searchNewsPage)
                    searchNews.postValue(handleSearchNewsResponse(response))
                } else {
                    Toast.makeText(
                        getApplication(),
                        "internet is not accessible",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } catch (t: Throwable) {

                when (t) {
                    is IOException -> Toast.makeText(
                        getApplication(),
                        "internet is not accessible",
                        Toast.LENGTH_SHORT
                    ).show()

                }

            }

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

    fun upsert(article: Article) = viewModelScope.launch {
        newsRepo.upsert(article)
    }

    fun getSavedNews() = newsRepo.getAllArticle()

    fun deleteArticle(article:Article) = viewModelScope.launch {
        newsRepo.deleteArticle(article)
    }


    fun isInternetAccessible( context: Context): Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

        }else{
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            return networkInfo.isConnected
        }
    }

}





