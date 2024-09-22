package com.kia.bookexplorer.ui.search

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(

): ViewModel() {

    private val searchEventsChannel = Channel<SearchEvents>()
    val bookListEvents = searchEventsChannel.receiveAsFlow()

}