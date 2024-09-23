package com.kia.bookexplorer.ui.book_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.kia.bookexplorer.data.network.dto.Book
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val book = savedStateHandle.getStateFlow<Book?>("book", null)
}