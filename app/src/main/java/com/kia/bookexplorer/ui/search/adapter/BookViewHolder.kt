package com.kia.bookexplorer.ui.search.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.kia.bookexplorer.R
import com.kia.bookexplorer.data.network.dto.Book
import com.kia.bookexplorer.databinding.ItemBookBinding

class BookViewHolder(
    private val binding: ItemBookBinding,
    private val listener: BookListener,
    private val context: Context
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun create(
            parent: ViewGroup,
            listener: BookListener,
        ): BookViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemBookBinding.inflate(inflater, parent, false)
            return BookViewHolder(binding, listener, parent.context)
        }
    }

    @SuppressLint("SetTextI18n")
    fun bind(book: Book) {

        binding.apply {

            // Set id for each book item card for animation transition
            ViewCompat.setTransitionName(
                clContainer,
                context.getString(R.string.book_card_transition_name, book.version.toString())
            )


            tvBookTitle.text = book.title
            tvBookAuthor.text = book.authorName?.fold("") { acc, s -> "$acc, $s" }?.substring(2)
            tvBookYear.text = book.firstPublishYear.toString()

            // load image to ivCover with coil and a placeholder
            if (book.coverI != 0) {
                ivBookCover.load("https://covers.openlibrary.org/b/id/${book.coverI}-M.jpg") {
                    crossfade(true)
                    placeholder(R.drawable.hard_cover_book)
                }
            } else
                ivBookCover.setImageResource(R.drawable.hard_cover_book)

            root.setOnClickListener {
                listener.onBookClicked(view = binding.root, book = book)
            }
        }
    }
}
