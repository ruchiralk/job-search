package com.rmunidasa.jobsearch.data

import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Format the date in to correct date string expected by JobSearchPagingSource
 * Instead of creating an extension function use this object to reuse the same DateFormatter
 * through the scope of JobSearch fragment
 */
class JSPagingKeyFormatter(
    private val pagingKeyFormatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE
) {

    // format locale date in to paging key
    fun pagingKey(date: LocalDate): String = date.format(pagingKeyFormatter)

    // generate next paging key based on current key
    fun nextKey(currentKey: String): String {
        val localDate = LocalDate.parse(currentKey, pagingKeyFormatter)
        val nextDay = localDate.plusDays(1)
        return nextDay.format(pagingKeyFormatter)
    }

    // generate previous paging key based on current key
    fun prevKey(currentKey: String): String {
        val localDate = LocalDate.parse(currentKey, pagingKeyFormatter)
        val nextDay = localDate.minusDays(1)
        return nextDay.format(pagingKeyFormatter)
    }

    // get local date from paging key
    fun localDate(key: String): LocalDate = LocalDate.parse(key, pagingKeyFormatter)

}