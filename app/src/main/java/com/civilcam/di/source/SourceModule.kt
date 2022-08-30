package com.civilcam.di.source

import com.civilcam.ui.network.source.SearchGuardiansDataSource
import org.koin.dsl.module

val sourceModule = module {

    factory { (query: String) -> SearchGuardiansDataSource(query, get()) }
}