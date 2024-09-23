package com.kia.bookexplorer.data.datasource

import androidx.paging.LoadState
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kia.bookexplorer.data.network.dto.Book
import com.kia.bookexplorer.data.network.service.BookService
import retrofit2.HttpException
import java.io.IOException

class BookListPagingSource(
    private val api: BookService,
    private val title: String,
    private val size: Int
) : PagingSource<Int, Book>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Book> {
        val position = params.key ?: STARTING_PAGE_INDEX

        return try {
            if(title.isEmpty()) return LoadResult.Error(Exception("There is no data!"))
            val response = api.getBooks(title,position/20 + 1, 20)

            val books = if (response.isSuccessful)
                response.body()!!.books
            else {
                emptyList()
            }

            if(books.isEmpty()){
                return LoadResult.Error(Exception("There is no data!"))
            } else {
                LoadResult.Page(
                    data = books,
                    prevKey = if (position == STARTING_PAGE_INDEX) null else position - size,
                    nextKey = if (books.isEmpty()) null else position + size
                )
            }



        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Book>): Int = 0

    companion object {
        const val STARTING_PAGE_INDEX = 1
    }
}