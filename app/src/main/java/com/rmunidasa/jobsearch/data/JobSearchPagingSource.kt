package com.rmunidasa.jobsearch.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rmunidasa.jobsearch.api.TemperApiService
import retrofit2.HttpException
import java.io.IOException
import java.time.LocalDate

class JobSearchPagingSource(
    private val apiService: TemperApiService,
    private val date: LocalDate,
    private val pagingKeyFormatter: JSPagingKeyFormatter
) : PagingSource<String, JobSearchPagingResult>() {

    /**
     * paging library could call this method over multiple scenarios
     * 1. when the user do a pull to refresh, at that moment anchorPosition will be null, just load
     *    from current key
     * 2. Api request failed and paging library needs refresh from last position, calculate the paging
     *    key based on anchorPosition
     */
    override fun getRefreshKey(state: PagingState<String, JobSearchPagingResult>): String {
        val defaultKey = pagingKeyFormatter.pagingKey(date)
        state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.let { page ->
                return when {
                    page.prevKey != null -> {
                        pagingKeyFormatter.nextKey(page.prevKey!!)
                    }
                    page.nextKey != null -> {
                        pagingKeyFormatter.prevKey(page.nextKey!!)
                    }
                    else -> {
                        defaultKey
                    }
                }
            }
        }
        return defaultKey
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, JobSearchPagingResult> {
        return try {
            val startKey = pagingKeyFormatter.pagingKey(date)
            val pagingKey = params.key ?: startKey

            // fetch jobs from api
            val pagedData = apiService.searchShifts(
                pagingKey
            ).data.map { shift ->
                JobSearchPagingResult(
                    pagingKeyFormatter.localDate(pagingKey),
                    shift
                )
            }
            // generate previous key only if paging key is not equal to start key since users
            // shouldn't be able to view expired jobs
            val prevKey = if (pagingKey != startKey) {
                pagingKeyFormatter.prevKey(pagingKey)
            } else {
                null
            }

            // generate next key from current key
            val nextKey = pagingKeyFormatter.nextKey(pagingKey)
            LoadResult.Page(
                data = pagedData,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }

    }
}