package com.pb.seenItAssignment.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.pb.seenItAssignment.R
import com.pb.seenItAssignment.data.repository.NetworkState
import kotlinx.android.synthetic.main.fragment_main.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class MainFragment : Fragment(), KodeinAware {

    override val kodein by kodein()
    private val factory:MainActivityViewModelFactory by instance<MainActivityViewModelFactory>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeLayout: SwipeRefreshLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_main, container, false)
        val viewModel= ViewModelProviders.of(this,factory).get(MainActivityViewModel::class.java)
        recyclerView=view.findViewById(R.id.rv_news_list);
        swipeLayout = view.findViewById(R.id.swipeLayout)
        viewModel.refreshData()
        val newsAdapter=NewsPagedListAdapter(requireContext())
        recyclerView.also {
            it.layoutManager= LinearLayoutManager(requireContext())
            it.setHasFixedSize(true)
            it.adapter=newsAdapter
        }
        swipeLayout.setOnRefreshListener {
            viewModel.refreshData()
        }

        viewModel.newsPagedList.observe(viewLifecycleOwner, Observer { articles->
            newsAdapter.submitList(articles)
            swipeLayout.isRefreshing = false

        })
        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            progress_bar_popular.visibility = if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error_popular.visibility = if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE
            if (!viewModel.listIsEmpty()) {
                newsAdapter.setNetworkState1(it)
            }
        })
        return view
    }


}
