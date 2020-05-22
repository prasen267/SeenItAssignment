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

    }
}
