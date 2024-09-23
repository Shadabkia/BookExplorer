package com.kia.bookexplorer.ui.book_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kia.bookexplorer.databinding.FragmentBookDetailsBinding
import com.kia.bookexplorer.ui.search.adapter.BookListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookDetailsFragment : Fragment(), BookListener {

    private val viewModel by viewModels<BookDetailsViewModel>()
    private var binding: FragmentBookDetailsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBookDetailsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniViews()
    }

    private fun iniViews() {

        initListeners()

        binding?.apply {

        }

    }

    private fun initListeners() {
        binding?.apply {

        }
    }

    override fun onBookClicked(position: Int, bookTitle: String?) {
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}