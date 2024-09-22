package com.kia.bookexplorer.data.repository

import androidx.paging.PagingData
import com.kia.bookexplorer.data.datasource.BookRemoteDataSource
import com.kia.bookexplorer.data.network.dto.Book
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BookRepositoryImp @Inject constructor(
    private val dataSource: BookRemoteDataSource
) : BookRepository {
    override suspend fun getBookList(title: String, page: Int): Flow<PagingData<Book>> =
        dataSource.getBookList(title = title, page = page)

}