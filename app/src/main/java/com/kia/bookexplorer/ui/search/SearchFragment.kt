package com.kia.bookexplorer.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.kia.bookexplorer.R
import com.kia.bookexplorer.databinding.FragmentSearchBinding
import com.kia.bookexplorer.ui.search.adapter.BookAdapter
import com.kia.bookexplorer.ui.search.adapter.BookListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.internal.notify
import timber.log.Timber

@AndroidEntryPoint
class SearchFragment : Fragment(), BookListener {

    private val viewModel by viewModels<SearchViewModel>()
    private var binding: FragmentSearchBinding? = null

    private val bookAdapter = BookAdapter(this)
    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniViews()
    }

    private fun iniViews() {

        initListeners()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.book.collectLatest {
                if (it != null) {
                    bookAdapter.submitData(it)
                } else {
                    bookAdapter.submitData(PagingData.empty())
                }
            }
        }

        binding?.apply {

            rvBooks.apply {
                adapter = bookAdapter
                itemAnimator = null
                postponeEnterTransition()
                viewTreeObserver.addOnPreDrawListener {
                    startPostponedEnterTransition()
                    true
                }
            }

            lifecycleScope.launch {
                bookAdapter.loadStateFlow.collectLatest {
                    Timber.tag("loadStateFlow")
                        .d("append ${it.append} prepend ${it.prepend} refresh ${it.refresh}")

                    pbLoadMore.isVisible = it.append is LoadState.Loading

                    if (it.refresh is LoadState.Error) {
                        tvNoResult.isVisible = true
                        tvNoResult.text = (it.refresh as LoadState.Error).error.message
                    } else {
                        tvNoResult.isVisible = false
                    }

                    pbMain.isVisible = it.refresh is LoadState.Loading
                }
            }
        }
    }

    private fun initListeners() {
        binding?.apply {
            svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    searchJob?.cancel()
                    searchJob = viewLifecycleOwner.lifecycleScope.launch {
                        newText?.let {
                            delay(330)
                            viewModel.clearBookList()
                            bookAdapter.refresh()
                            binding?.rvBooks?.scrollToPosition(0)
                            viewModel.getBookList(it)
                        }
                    }
                    return true
                }
            })
        }
    }


    override fun onBookClicked(position: Int, bookTitle: String?) {
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}