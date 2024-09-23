package com.kia.bookexplorer.data.network

import com.kia.bookexplorer.data.local.APPLICATION_JSON_HEADER_KEY
import com.kia.bookexplorer.data.local.AUTHORIZATION_HEADER_KEY
import com.kia.bookexplorer.data.local.CONTENT_TYPE_HEADER_KEY
import com.kia.bookexplorer.data.local.TOKEN_PREFIX
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class HeaderInterceptor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        requestBuilder.addHeader(
            CONTENT_TYPE_HEADER_KEY, APPLICATION_JSON_HEADER_KEY
        )

//        requestBuilder.addHeader(
//            AUTHORIZATION_HEADER_KEY, TOKEN_PREFIX + ACCESS_TOKEN
//        )
        Timber.tag("okhttp Header: ").i("Token: Bearer $ACCESS_TOKEN")

        val request = requestBuilder.build()

        return chain.proceed(request)
    }

    companion object {
        const val ACCESS_TOKEN = ""
    }
}