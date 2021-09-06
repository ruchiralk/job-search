package com.rmunidasa.jobsearch.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.rmunidasa.jobsearch.api.TemperApiService
import java.time.LocalDate
import javax.inject.Inject

class JobSearchRepository @Inject constructor(private val temperService: TemperApiService) {

    private val pagingKeyFormatter = JSPagingKeyFormatter()

    // keep a reference to the current paging source inorder to invalidate current data on
    // pull to refresh
    private var pagingSource: JobSearchPagingSource? = null

    private fun createPagingSource(date: LocalDate): JobSearchPagingSource {
        pagingSource = JobSearchPagingSource(
            temperService,
            date,
            pagingKeyFormatter
        )
        return pagingSource!!
    }

    fun getSearchResults(
        filterDate: LocalDate,
        pageSize: Int = 10,
        maxSize: Int = 30
    ): LiveData<PagingData<JobSearchPagingResult>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                maxSize = maxSize, // recyclerview starts dropping items after this point
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                createPagingSource(filterDate)
            }
        ).liveData
    }

    fun invalidate() {
        pagingSource?.invalidate()
    }
}