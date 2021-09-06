package com.rmunidasa.jobsearch.api

import com.rmunidasa.jobsearch.model.Shift

data class JobSearchResponse(
    val `data`: List<Shift>
)
