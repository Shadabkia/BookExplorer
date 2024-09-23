package com.kia.bookexplorer.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kia.bookexplorer.data.network.dto.Book
import com.kia.bookexplorer.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
private val bookRepository: BookRepository
): ViewModel() {

    private val _book = MutableStateFlow<PagingData<Book>?>(null)
    val book: StateFlow<PagingData<Book>?> get() = _book

    fun getBookList(title : String) = viewModelScope.launch {
        bookRepository.getBookList(title, 1)
            .cachedIn(viewModelScope)
            .collectLatest { result ->
                _book.value = result
            }
    }

    fun clearBookList() {
        _book.value = PagingData.empty()
    }

}