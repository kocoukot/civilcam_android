package com.civilcam.alert_feature.list.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import coil.network.HttpException
import com.civilcam.domainLayer.ServiceException
import com.civilcam.domainLayer.model.PaginationRequest
import com.civilcam.domainLayer.model.alerts.AlertModel
import com.civilcam.domainLayer.repos.AlertsRepository
import java.io.IOException

class AlertListDataSource(
    private val alertsRepository: AlertsRepository
) : PagingSource<Int, AlertModel>() {

    override fun getRefreshKey(state: PagingState<Int, AlertModel>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AlertModel> {
        val currentKey = params.key ?: 1
        val previousKey = if (currentKey == 1) null else currentKey - 1
        return try {
            alertsRepository.getAlertsList(
                PaginationRequest.Pagination(currentKey, params.loadSize)
            ).let { orders ->
                val nextKey: Int? =
                    if (orders.size < params.loadSize) null else currentKey + 1
                val loadResult: LoadResult<Int, AlertModel> =
                    LoadResult.Page(orders, previousKey, nextKey)
                loadResult
            }
        } catch (exception: ServiceException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        }
    }
}