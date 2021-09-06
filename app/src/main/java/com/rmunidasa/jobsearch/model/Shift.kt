package com.rmunidasa.jobsearch.model

data class Shift(
    val average_estimated_earnings_per_hour: AverageEstimatedEarningsPerHour,
    val created_at: String,
    val distance: Any?,
    val duration: Int,
    val earliest_possible_start_time: String,
    val earnings_per_hour: EarningsPerHour,
    val ends_at: String,
    val id: String,
    val job: Job,
    val latest_possible_end_time: String,
    val start_time_earlier_variation: Int,
    val start_time_later_variation: Int,
    val starts_at: String,
) {

    data class AverageEstimatedEarningsPerHour(
        val amount: Double,
        val currency: String
    )

    data class EarningsPerHour(
        val amount: Double,
        val currency: String
    )

    data class Job(
        val category: Category,
        val id: String,
        val project: Project,
        val title: String,
        val report_at_address: ReportAtAddress,
    )

    data class Category(
        val id: String,
        val name: String,
        val name_translation: NameTranslation,
    )

    data class NameTranslation(
        val en_GB: String,
        val nl_NL: String
    )

    data class Project(
        val client: Client,
        val id: String,
        val name: String
    )

    data class ReportAtAddress(
        val city: String,
        val country: Country,
        val extra: String,
        val geo: Geo,
        val line1: String,
        val line2: String,
        val links: AddressLinks,
        val number: String,
        val number_with_extra: String,
        val region: String,
        val street: String,
        val zip_code: String
    )

    data class Client(
        val description: String?,
        val id: String,
        val links: ClientLinks,
        val name: String
    )

    data class ClientLinks(
        val hero_image: String,
        val thumb_image: String
    )

    data class Country(
        val human: String,
        val iso3166_1: String
    )

    data class Geo(
        val lat: Double,
        val lon: Double
    )

    data class AddressLinks(
        val get_directions: String
    )

}
