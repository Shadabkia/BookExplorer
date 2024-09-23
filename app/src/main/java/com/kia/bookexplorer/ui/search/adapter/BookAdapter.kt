package com.kia.bookexplorer.ui.search.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.kia.bookexplorer.data.network.dto.Book

class BookAdapter(
    private val listener : BookListener
) : PagingDataAdapter<Book, BookViewHolder>(DiffCallBack()) {

    private class DiffCallBack : DiffUtil.ItemCallback<Book>() {

        override fun areItemsTheSame(oldItem: Book, newItem: Book) =
            oldItem.version == newItem.version

        override fun areContentsTheSame(
            oldItem: Book,
            newItem: Book
        ) =
            oldItem == newItem

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BookViewHolder.create(parent, listener)

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }


}