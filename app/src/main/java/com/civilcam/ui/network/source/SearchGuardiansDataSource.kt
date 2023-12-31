package com.civilcam.ui.network.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.civilcam.domainLayer.ServiceException
import com.civilcam.domainLayer.model.PaginationRequest
import com.civilcam.domainLayer.model.guard.PersonModel
import com.civilcam.domainLayer.repos.GuardiansRepository
import retrofit2.HttpException
import java.io.IOException

class SearchGuardiansDataSource(
    private val query: String,
    private val guardiansRepository: GuardiansRepository
) : PagingSource<Int, PersonModel>() {

    override fun getRefreshKey(state: PagingState<Int, PersonModel>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PersonModel> {
        val currentKey = params.key ?: 1
        val previousKey = if (currentKey == 1) null else currentKey - 1
        return try {
            query.takeIf { it.isNotEmpty() }
                ?.let {
                    guardiansRepository.searchGuardian(
                        query,
                        PaginationRequest.Pagination(currentKey, params.loadSize)
                    ).let { orders ->
                        val nextKey: Int? =
                            if (orders.size < params.loadSize) null else currentKey + 1
                        val loadResult: LoadResult<Int, PersonModel> =
                            LoadResult.Page(orders, previousKey, nextKey)
                        loadResult
                    }
                } ?: kotlin.run {
                LoadResult.Page(emptyList(), previousKey, null)
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