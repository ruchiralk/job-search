package com.rmunidasa.jobsearch.ui.jobsearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.rmunidasa.jobsearch.R
import com.rmunidasa.jobsearch.databinding.JobSearchViewItemBinding
import com.rmunidasa.jobsearch.databinding.SeparatorViewItemBinding

class JobSearchAdapter : PagingDataAdapter<UiModel, JobSearchViewHolder>(SHIFT_COMPARATOR) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is UiModel.SeparatorItem -> R.layout.separator_view_item
            is UiModel.ShiftItem -> R.layout.job_search_view_item
            else -> throw IllegalArgumentException("view type not defined")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobSearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.separator_view_item -> {
                val binding = SeparatorViewItemBinding.inflate(inflater, parent, false)
                JobSearchViewHolder.SeparatorViewHolder(binding)
            }
            R.layout.job_search_view_item -> {
                val binding =
                    JobSearchViewItemBinding.inflate(
                        inflater,
                        parent,
                        false
                    )
                JobSearchViewHolder.SearchResultItemViewHolder(binding)
            }
            else -> throw java.lang.IllegalArgumentException("invalid view type")
        }
    }

    override fun onBindViewHolder(holder: JobSearchViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            when (holder) {
                is JobSearchViewHolder.SeparatorViewHolder -> holder.bind(currentItem as UiModel.SeparatorItem)
                is JobSearchViewHolder.SearchResultItemViewHolder -> holder.bind(currentItem as UiModel.ShiftItem)
            }
        }
    }

    companion object {
        private val SHIFT_COMPARATOR = object : DiffUtil.ItemCallback<UiModel>() {

            override fun areItemsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
                return (oldItem is UiModel.SeparatorItem && newItem is UiModel.SeparatorItem && oldItem.label == newItem.label)
                        || (oldItem is UiModel.ShiftItem && newItem is UiModel.ShiftItem && oldItem.result.shift == newItem.result.shift)
            }

            override fun areContentsTheSame(
                oldItem: UiModel,
                newItem: UiModel
            ): Boolean = oldItem == newItem

        }
    }
}