package com.kia.bookexplorer.data.datasource

import androidx.paging.PagingData
import com.kia.bookexplorer.data.network.dto.Book
import kotlinx.coroutines.flow.Flow

interface BookRemoteDataSource {
    suspend fun getBookList(
        title: String,
        page: Int
    ): Flow<PagingData<Book>>
}