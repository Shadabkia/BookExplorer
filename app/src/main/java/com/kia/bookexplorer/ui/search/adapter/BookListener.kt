package com.kia.bookexplorer.ui.search.adapter

import android.view.View
import com.kia.bookexplorer.data.network.dto.Book

interface BookListener {
    fun onBookClicked(view : View, book: Book)
}
