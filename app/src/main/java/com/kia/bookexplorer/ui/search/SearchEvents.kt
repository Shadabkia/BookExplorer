package com.kia.bookexplorer.ui.search

import android.view.View

sealed class SearchEvents {
    class SendMessage(val message: String?) : SearchEvents()
    data object InitView : SearchEvents()
}