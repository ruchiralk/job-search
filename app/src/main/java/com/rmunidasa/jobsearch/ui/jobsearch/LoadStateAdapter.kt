package com.rmunidasa.jobsearch.ui.jobsearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rmunidasa.jobsearch.databinding.LoadStateFooterBinding

/**
 * Adapter to show loading state if the user is waiting for more data
 *
 * If the load request fail, retry button will be shown at the recyclerview footer
 * with the error message
 */
class LoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<com.rmunidasa.jobsearch.ui.jobsearch.LoadStateAdapter.LoadStateViewHolder>() {

    override fun onBindViewHolder(
        holder: LoadStateViewHolder,
        loadState: LoadState
    ) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadStateViewHolder {
        val binding = LoadStateFooterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LoadStateViewHolder(binding)
    }

    inner class LoadStateViewHolder(private val binding: LoadStateFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.buttonRetry.setOnClickListener {
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState) {
            binding.apply {
                progressBar.isVisible = loadState is LoadState.Loading
                buttonRetry.isVisible = loadState !is LoadState.Loading
                textViewError.isVisible = loadState !is LoadState.Loading
            }
        }
    }
}