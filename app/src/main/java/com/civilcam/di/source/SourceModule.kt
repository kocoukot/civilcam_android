package com.civilcam.di.source

import com.civilcam.alert_feature.history.source.AlertHistoryListDataSource
import com.civilcam.alert_feature.list.source.AlertListDataSource
import com.civilcam.ui.network.source.SearchGuardiansDataSource
import org.koin.dsl.module

val sourceModule = module {

    factory { (query: String) -> SearchGuardiansDataSource(query, get()) }

    factory { AlertListDataSource(get()) }

    factory { (type: String) -> AlertHistoryListDataSource(type, get()) }


}