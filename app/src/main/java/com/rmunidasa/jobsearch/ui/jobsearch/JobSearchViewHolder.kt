package com.rmunidasa.jobsearch.ui.jobsearch

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.rmunidasa.jobsearch.databinding.JobSearchViewItemBinding
import com.rmunidasa.jobsearch.databinding.SeparatorViewItemBinding
import com.rmunidasa.jobsearch.model.Shift
import java.text.NumberFormat
import java.util.*

sealed class JobSearchViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    class SearchResultItemViewHolder(private val binding: JobSearchViewItemBinding) :
        JobSearchViewHolder(binding) {

        fun bind(item: UiModel.ShiftItem) {
            val job = item.result.shift.job
            binding.apply {

                // configure glide to lazy load images
                Glide.with(itemView)
                    .asBitmap()
                    .load(job.project.client.links.heroImage)
                    .downsample(DownsampleStrategy.CENTER_OUTSIDE)
                    .placeholder(ColorDrawable(Color.GRAY))
                    .fallback(ColorDrawable(Color.GRAY))
                    .error(ColorDrawable(Color.GRAY))
                    .into(imageView)

                categoryTextview.text = job.category.name.uppercase()
                labelTextview.text = job.project.client.name
                durationTextview.text =
                    formatDuration(item.result.shift.startsAt, item.result.shift.endsAt)
                payTextView.text = formattedCurrencyText(item.result.shift.earningsPerHour)
            }
        }

        // format duration to be in HH:mm - HH:mm format
        private fun formatDuration(startDateTime: String, endDateTime: String): String {
            val startTime = extractTime(startDateTime)
            val endTime = extractTime(endDateTime)
            return "$startTime - $endTime"

        }

        /**
         * Shift starts_at and ends_at properties return datetime in multiple formats
         * instead of initializing multiple date formatters, use a less expensive regex
         * to extract HH:mm from any date time format
         */
        private fun extractTime(dateTime: String): String? {
            return REGEX_EXTRACT_DURATION.find(dateTime)?.value
        }

        /**
         * Returns shift earnings per hour in currency format according to localization
         */
        private fun formattedCurrencyText(earning: Shift.EarningsPerHour): String {
            val formatter = NumberFormat.getCurrencyInstance()
            formatter.isGroupingUsed = true
            formatter.maximumFractionDigits = 2
            formatter.minimumFractionDigits = 2
            formatter.currency = Currency.getInstance(earning.currency)
            return formatter.format(earning.amount)
        }
    }

    class SeparatorViewHolder(private val binding: SeparatorViewItemBinding) :
        JobSearchViewHolder(binding) {
        fun bind(item: UiModel.SeparatorItem) {
            binding.headerTextView.text = item.label
        }
    }

    private companion object {
        private val REGEX_EXTRACT_DURATION = "((?:[01]\\d|2[0-3]):[0-5]\\d)".toRegex()
    }
}