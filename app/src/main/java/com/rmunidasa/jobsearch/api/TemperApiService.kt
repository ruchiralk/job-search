package com.rmunidasa.jobsearch.api

import retrofit2.http.GET
import retrofit2.http.Query

interface TemperApiService {

    @GET("shifts")
    suspend fun searchShifts(
        @Query("filter[date]") date: String
    ): JobSearchResponse

    companion object {
        const val BASE_URL = "https://temper.works/api/v3/"
    }
}