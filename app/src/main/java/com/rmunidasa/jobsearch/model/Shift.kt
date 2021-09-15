package com.rmunidasa.jobsearch.model

import com.squareup.moshi.Json

data class Shift(
    @Json(name = "earnings_per_hour")
    val earningsPerHour: EarningsPerHour,
    @Json(name = "ends_at")
    val endsAt: String,
    val id: String,
    val job: Job,
    @Json(name = "starts_at")
    val startsAt: String,
) {

    data class EarningsPerHour(
        val amount: Double,
        val currency: String
    )

    data class Job(
        val category: Category,
        val id: String,
        val project: Project,
        val title: String
    )

    data class Project(
        val client: Client,
        val id: String,
        val name: String
    )

    data class Category(
        val id: String,
        val name: String
    )

    data class Client(
        val description: String?,
        val id: String,
        val links: ClientLinks,
        val name: String
    )

    data class ClientLinks(
        @Json(name = "hero_image")
        val heroImage: String,
        @Json(name = "thumb_image")
        val thumbImage: String
    )
}
