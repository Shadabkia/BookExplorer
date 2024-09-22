package com.kia.bookexplorer.data.network.service

import com.kia.bookexplorer.data.network.dto.BookResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {

    @GET("search.json")
    suspend fun getBooks(
        @Query("title") title: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
    ): Response<BookResponse>
}