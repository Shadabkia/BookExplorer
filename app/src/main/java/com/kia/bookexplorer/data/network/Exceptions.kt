package com.kia.bookexplorer.data.network

import java.io.IOException

class ApiException(message: String) : IOException(message)

class NoInternetException(message: String) : IOException(message)