package com.kia.bookexplorer.ui.search.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.kia.bookexplorer.R
import com.kia.bookexplorer.data.network.dto.Book
import com.kia.bookexplorer.databinding.ItemBookBinding

class BookViewHolder(
    private val binding: ItemBookBinding,
    private val listener: BookListener,
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun create(
            parent: ViewGroup,
            listener: BookListener,
        ): BookViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemBookBinding.inflate(inflater, parent, false)
            return BookViewHolder(binding, listener)
        }
    }

    @SuppressLint("SetTextI18n")
    fun bind(book: Book) {

        binding.apply {

            tvBookTitle.text = book.title
            tvBookAuthor.text = book.authorName?.fold("") { acc, s -> "$acc, $s" }?.substring(2)
            tvBookYear.text = book.firstPublishYear.toString()

            // load image to ivCover with coil and a placeholder https://covers.openlibrary.org/b/id/10535178-s.jpg
            ivBookCover.load("https://covers.openlibrary.org/b/id/"+book.coverI+"-M.jpg") {
                crossfade(true)
                placeholder(R.drawable.ic_avatar)
            }

            root.setOnClickListener {
                listener.onBookClicked(position = bindingAdapterPosition, book.title)
            }
        }
    }
}
