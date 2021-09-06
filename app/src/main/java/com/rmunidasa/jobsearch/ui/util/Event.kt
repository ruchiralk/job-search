package com.rmunidasa.jobsearch.ui.util

/**
 * Wrapper created to observe LiveData events only once
 */
class Event<out T>(private val content: T) {

    var consumed = false
        private set

    /**
     * Consume the content if it's not been consumed yet
     */
    fun consume(): T? {
        return if (consumed) {
            null
        } else {
            consumed = true
            content
        }
    }
}