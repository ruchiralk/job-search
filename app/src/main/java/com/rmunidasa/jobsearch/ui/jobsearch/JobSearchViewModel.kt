package com.rmunidasa.jobsearch.ui.jobsearch

import androidx.lifecycle.*
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.rmunidasa.jobsearch.data.JobSearchPagingResult
import com.rmunidasa.jobsearch.model.JobSearchRepository
import com.rmunidasa.jobsearch.ui.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class JobSearchViewModel @Inject constructor(
    private val jobSearchRepository: JobSearchRepository
) : ViewModel() {

    val login: LiveData<Event<Boolean>>
        get() = _login
    private val _login = MutableLiveData<Event<Boolean>>()

    val register: LiveData<Event<Boolean>>
        get() = _register
    private val _register = MutableLiveData<Event<Boolean>>()

    // select initial page key, at the moment always set to current date
    private val filterDate = MutableLiveData(LocalDate.now())

    // format section header date
    private val separatorDateFormatter = DateTimeFormatter.ofPattern("EEEE dd MMMM")

    val searchResults = filterDate.switchMap { date ->
        jobSearchRepository
            .getSearchResults(date)
            .map { pagingData -> pagingData.map { UiModel.ShiftItem(it) } }
            .map {
                // logically separate jobs based on date
                it.insertSeparators { before, after ->
                    if (after == null) {
                        // at the end of the list
                        return@insertSeparators null
                    }
                    if (before == null) {
                        // we are at the beginning
                        return@insertSeparators UiModel.SeparatorItem(
                            formatSeparatorDate(after.result.key)
                        )
                    }
                    if (before.result.key != after.result.key) {
                        // first item of different page(date), insert a separator
                        UiModel.SeparatorItem(
                            formatSeparatorDate(after.result.key)
                        )
                    } else {
                        // no separator
                        null
                    }
                }
            }.cachedIn(viewModelScope) // can't load from same paging data twice

    }

    /**
     * Refresh data when user performs a pull to refresh
     */
    fun refresh() {
        jobSearchRepository.invalidate()
    }

    /**
     * Navigate to login screen on login button tap
     */
    fun login() {
        _login.value = Event(true)
    }

    /**
     * Navigate to register screen on register button tap
     */
    fun register() {
        _register.value = Event(true)
    }

    private fun formatSeparatorDate(date: LocalDate): String {
        return separatorDateFormatter.format(date).lowercase()
    }
}

sealed class UiModel {
    data class ShiftItem(val result: JobSearchPagingResult) : UiModel()
    data class SeparatorItem(val label: String?) : UiModel()
}