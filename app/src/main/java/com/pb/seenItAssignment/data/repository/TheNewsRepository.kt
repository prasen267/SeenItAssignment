package com.pb.seenItAssignment.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.pb.seenItAssignment.data.api.NEWS_PER_PAGE
import com.pb.seenItAssignment.data.api.TheNewService
import com.pb.seenItAssignment.data.model.Article
import com.pb.seenItAssignment.data.model.NewsResponse
import io.reactivex.rxjava3.disposables.CompositeDisposable

class TheNewsRepository(private val theNewService: TheNewService ) {
   lateinit var newsPagedList:LiveData<PagedList<Article>>
    lateinit var newsDataSourceFactory: NewsDataSourceFactory
    fun fetchLiveNewsPagedList(compositeDisposable: CompositeDisposable):LiveData<PagedList<Article>>{

        newsDataSourceFactory= NewsDataSourceFactory(theNewService,compositeDisposable)
        val config=PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(10)
            .build()
        newsPagedList= LivePagedListBuilder(newsDataSourceFactory,config).build()
        return newsPagedList
    }
    fun getNetworkState1(): LiveData<NetworkState> {
        return Transformations.switchMap(
            newsDataSourceFactory.newsLiveDataSource, NewsDataSource::networkState)
    }
    fun refresh(){
        if(this::newsDataSourceFactory.isInitialized) {
            newsDataSourceFactory.newsLiveDataSource.value!!.invalidate()

        }
    }
}