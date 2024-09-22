package com.kia.bookexplorer.di

import com.kia.bookexplorer.MyApplication
import com.kia.bookexplorer.data.local.BASE_URL
import com.kia.bookexplorer.data.network.CacheInterceptor
import com.kia.bookexplorer.data.network.HeaderInterceptor
import com.kia.bookexplorer.data.network.NetworkConnectionInterceptor
import com.kia.bookexplorer.data.network.service.BookService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideDispatcher(): Dispatcher {
        val dispatcher = Dispatcher()
        dispatcher.maxRequests = 1
        return dispatcher
    }

    @Singleton
    @Provides
    fun provideLogging(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    @Singleton
    @Provides
    fun provideHeaderInterceptor() =
        HeaderInterceptor()

    @Singleton
    @Provides
    fun provideCacheInterceptor(application: MyApplication) =
        CacheInterceptor(application)

    @Singleton
    @Provides
    fun provideNetworkConnectionInterceptor(context: MyApplication) =
        NetworkConnectionInterceptor(context = context)

    @Singleton
    @Provides
    fun provideHttpClient(
        logging: HttpLoggingInterceptor,
        headerInterceptor: HeaderInterceptor,
        cacheInterceptor: CacheInterceptor,
        networkConnectionInterceptor: NetworkConnectionInterceptor,
        dispatcher: Dispatcher,
        application: MyApplication
    ): OkHttpClient {

        val cacheSize = (50 * 1024 * 1024).toLong()
        val myCache = Cache(application.cacheDir, cacheSize)

        return OkHttpClient.Builder()
            .cache(myCache)
            .addInterceptor(logging)
            .addInterceptor(headerInterceptor)
//            .addInterceptor(networkConnectionInterceptor)
//            .addInterceptor(cacheInterceptor)
            .connectTimeout(10, TimeUnit.SECONDS) // connect timeout
            .writeTimeout(10, TimeUnit.SECONDS) // write timeout
            .readTimeout(10, TimeUnit.SECONDS) // read timeout
            .retryOnConnectionFailure(true)
            .dispatcher(dispatcher)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(okHttpClient: OkHttpClient): BookService {

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())  // Add this line
            .build()

       return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(BookService::class.java)
    }
}