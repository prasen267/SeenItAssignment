package com.pb.seenItAssignment.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.pb.seenItAssignment.data.api.FIRST_PAGE
import com.pb.seenItAssignment.data.api.TheNewService
import com.pb.seenItAssignment.data.model.Article
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class NewsDataSource(
    private val theNewService: TheNewService,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int, Article>() {

    private var page= FIRST_PAGE
    val networkState= MutableLiveData<NetworkState>()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Article>
    ) {
        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            theNewService.getTopHeadLines(page)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    callback.onResult(it.articles,null,page+1)
                    networkState.postValue(NetworkState.LOADED)
                },{
                    networkState.postValue(NetworkState.ERROR)
                    Log.e("PAGEDKEYDATASOURCE",it.message)
                })

        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Article>) {

        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            theNewService.getTopHeadLines(params.key)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if(it.totalResults/10+1>=params.key){
                        callback.onResult(it.articles,params.key+1)
                        networkState.postValue(NetworkState.LOADED)
                    }
                    else{
                        networkState.postValue(NetworkState.ENDOFLIST)
                    }
                },{
                    networkState.postValue(NetworkState.ERROR)
                    Log.e("PAGEDKEYDATASOURCE",it.message)
                })

        )

    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Article>) {

    }

}