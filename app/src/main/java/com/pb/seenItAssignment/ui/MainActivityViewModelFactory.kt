package com.pb.seenItAssignment.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pb.seenItAssignment.data.repository.TheNewsRepository

class MainActivityViewModelFactory(
    private val repository: TheNewsRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainActivityViewModel(repository) as T
    }
}