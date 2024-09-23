package com.kia.bookexplorer.ui.search

import android.view.View

sealed class SearchEvents {
    class NavigateToBookDetails(val view : View, val bookId : Long): SearchEvents()
    class SendMessage(val message: String?) : SearchEvents()
}