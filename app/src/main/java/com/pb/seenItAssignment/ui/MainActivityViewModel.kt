package com.pb.seenItAssignment.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.pb.seenItAssignment.data.model.Article
import com.pb.seenItAssignment.data.repository.NetworkState
import com.pb.seenItAssignment.data.repository.TheNewsRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable

class MainActivityViewModel(private val repository: TheNewsRepository):ViewModel() {

    private val compositeDisposable= CompositeDisposable()
    val newsPagedList: LiveData<PagedList<Article>> by lazy {
        repository.fetchLiveNewsPagedList(compositeDisposable)
    }
    val networkState:LiveData<NetworkState> by lazy {
        repository.getNetworkState1()
    }
    fun listIsEmpty():Boolean{
        return newsPagedList.value?.isEmpty()?:true
    }
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun refreshData() {
       repository.refresh()
        Thread.sleep(500) // including a small delay to help everything settle in
      //newsPagedList= repository.fetchLiveNewsPagedList(compositeDisposable)
    }
}