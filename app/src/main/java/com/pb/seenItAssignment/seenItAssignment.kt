package com.pb.seenItAssignment

import android.app.Application
import com.pb.seenItAssignment.data.api.TheNewService
import com.pb.seenItAssignment.data.repository.TheNewsRepository
import com.pb.seenItAssignment.ui.MainActivityViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class seenItAssignment:Application(), KodeinAware {
    override val kodein= Kodein.lazy {

        import(androidXModule(this@seenItAssignment))
        bind()from singleton { TheNewService() }
        bind() from singleton { TheNewsRepository(instance()) }

        bind() from provider { MainActivityViewModelFactory(instance()) }
    }
}