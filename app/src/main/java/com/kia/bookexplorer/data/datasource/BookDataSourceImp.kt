package com.kia.bookexplorer.data.datasource

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kia.bookexplorer.data.network.dto.Book
import com.kia.bookexplorer.data.network.service.BookService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BookDataSourceImp @Inject constructor(
    private val service: BookService,
) : BookRemoteDataSource {
    override suspend fun getBookList(title: String ,page: Int): Flow<PagingData<Book>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                BookListPagingSource(service, title, 20)
            }
        ).flow
    }
}