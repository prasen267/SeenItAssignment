package com.pb.seenItAssignment.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.pb.seenItAssignment.R
import com.pb.seenItAssignment.data.repository.NetworkState
import kotlinx.android.synthetic.main.activity_main.*


import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class MainActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()
    private val factory:MainActivityViewModelFactory by instance<MainActivityViewModelFactory>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewModel= ViewModelProviders.of(this,factory).get(MainActivityViewModel::class.java)
        val newsAdapter=NewsPagedListAdapter(this)
        rv_news_list.also {
            it.layoutManager=LinearLayoutManager(this)
            it.setHasFixedSize(true)
            it.adapter=newsAdapter
        }
        swipeLayout.setOnRefreshListener {
            viewModel.refreshData()
        }

        viewModel.newsPagedList.observe(this, Observer { articles->
           newsAdapter.submitList(articles)
            swipeLayout.isRefreshing = false

        })
        viewModel.networkState.observe(this, Observer {
            progress_bar_popular.visibility = if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error_popular.visibility = if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE
            if (!viewModel.listIsEmpty()) {
                newsAdapter.setNetworkState1(it)
            }
        })
    }
}
