package com.pb.seenItAssignment.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.pb.seenItAssignment.data.api.TheNewService
import com.pb.seenItAssignment.data.model.Article
import io.reactivex.rxjava3.disposables.CompositeDisposable

class NewsDataSourceFactory(
    private val theNewService: TheNewService,
    private val compositeDisposable: CompositeDisposable
) : DataSource.Factory<Int, Article>() {
    val newsLiveDataSource = MutableLiveData<NewsDataSource>()
    override fun create(): DataSource<Int, Article> {
        val newsDataSource=NewsDataSource(theNewService,compositeDisposable)
        newsLiveDataSource.postValue(newsDataSource)
        return newsDataSource
    }
}