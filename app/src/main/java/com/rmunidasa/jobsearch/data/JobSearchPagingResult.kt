package com.rmunidasa.jobsearch.data

import com.rmunidasa.jobsearch.model.Shift
import java.time.LocalDate

/**
 * Wrapper created to keep track of paging key & the related shift
 */
data class JobSearchPagingResult(
    val key: LocalDate,
    val shift: Shift
)
