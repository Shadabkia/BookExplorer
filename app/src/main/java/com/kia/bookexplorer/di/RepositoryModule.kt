package com.kia.bookexplorer.di

import com.kia.bookexplorer.data.repository.BookRepository
import com.kia.bookexplorer.data.repository.BookRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindBookRepository(bookRepository: BookRepositoryImp): BookRepository

}