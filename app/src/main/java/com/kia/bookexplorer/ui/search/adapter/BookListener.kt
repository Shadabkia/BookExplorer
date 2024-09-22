package com.kia.bookexplorer.ui.search.adapter

import android.view.View

interface BookListener {
    fun onBookClicked(position: Int, bookTitle: String?)
}
