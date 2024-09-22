package com.kia.bookexplorer.data.repository

import androidx.paging.PagingData
import com.kia.bookexplorer.data.network.dto.Book
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    suspend fun getBookList(title: String, page: Int) : Flow<PagingData<Book>>
}