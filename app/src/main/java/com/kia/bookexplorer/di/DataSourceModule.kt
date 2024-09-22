package com.kia.bookexplorer.di

import com.kia.bookexplorer.data.datasource.BookDataSourceImp
import com.kia.bookexplorer.data.datasource.BookRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindDatasource(dataSource: BookDataSourceImp): BookRemoteDataSource


}