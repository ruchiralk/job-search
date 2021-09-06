package com.rmunidasa.jobsearch.ui.jobsearch

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.rmunidasa.jobsearch.R
import com.rmunidasa.jobsearch.databinding.FragmentJobSearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JobSearchFragment : Fragment(R.layout.fragment_job_search) {

    private val viewModel: JobSearchViewModel by viewModels()

    private var _binding: FragmentJobSearchBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentJobSearchBinding.bind(view)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.bindSwipeToRefresh()
        val adapter = JobSearchAdapter()
        binding.bindList(adapter, viewModel.searchResults)
        binding.bindLoadingProgress(adapter)
        configureNavigation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun FragmentJobSearchBinding.bindList(
        jobSearchAdapter: JobSearchAdapter,
        searchResults: LiveData<PagingData<UiModel>>
    ) {
        recyclerView.setHasFixedSize(true)
        // paging v3 library supports a footer adapter to show loading state when user is waiting to
        // load more data
        recyclerView.adapter = jobSearchAdapter.withLoadStateFooter(
            footer = LoadStateAdapter { jobSearchAdapter.retry() }
        )
        fun isHeader(position: Int): Boolean {
            val viewType = jobSearchAdapter.getItemViewType(position)
            return viewType == R.layout.separator_view_item
        }
        // Add sticky section header to separate shifts by the date
        recyclerView.addItemDecoration(
            HeaderItemDecoration(recyclerView, false, ::isHeader)
        )
        searchResults.observe(viewLifecycleOwner) {
            if (swipeRefreshLayout.isRefreshing) {
                swipeRefreshLayout.isRefreshing = false
            }
            jobSearchAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    private fun FragmentJobSearchBinding.bindLoadingProgress(
        jobSearchAdapter: JobSearchAdapter,
    ) {
        jobSearchAdapter.addLoadStateListener { loadState ->
            val isListEmpty =
                loadState.refresh is LoadState.NotLoading && jobSearchAdapter.itemCount == 0
            // show empty list
            emptyList.isVisible = isListEmpty
            // Only show the list if refresh succeeds
            recyclerView.isVisible = !isListEmpty
            // Show loading spinner during initial load or refresh.
            progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
                ?: loadState.refresh as? LoadState.Error
            errorState?.let {
                Toast.makeText(
                    this@JobSearchFragment.requireContext(),
                    "${it.error}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun FragmentJobSearchBinding.bindSwipeToRefresh() {
        swipeRefreshLayout.setOnRefreshListener {
            viewModel?.refresh()
        }
    }


    private fun configureNavigation() {
        configureLoginNavigation()
        configureRegisterNavigation()
    }

    // Navigate to login screen on login button press
    private fun configureLoginNavigation() {
        viewModel.login.observe(viewLifecycleOwner) {
            if (it.consume() == true) {
                findNavController().navigate(R.id.search_to_login_destination)
            }
        }
    }

    // Navigate to register screen on register button press
    private fun configureRegisterNavigation() {
        viewModel.register.observe(viewLifecycleOwner) {
            if (it.consume() == true) {
                findNavController().navigate(R.id.search_to_register_destination)
            }
        }
    }
}