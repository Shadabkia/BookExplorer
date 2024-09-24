package com.kia.bookexplorer.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.google.android.material.transition.MaterialElevationScale
import com.kia.bookexplorer.R
import com.kia.bookexplorer.data.network.dto.Book
import com.kia.bookexplorer.databinding.FragmentSearchBinding
import com.kia.bookexplorer.ui.search.adapter.BookAdapter
import com.kia.bookexplorer.ui.search.adapter.BookListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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

        // Show popup transition animation from BookDetailsFragment
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        // To save fragment state when navigate up from next fragment
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchEvents.collect { event ->
                    when (event) {
                        SearchEvents.InitView -> initViews()
                        is SearchEvents.SendMessage -> Toast.makeText(requireContext(), event.message, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }
        viewModel.fragmentCreated()

    }

    private fun initViews() {

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
//                            binding?.rvBooks?.scrollToPosition(0)
                            viewModel.getBookList(it)
                        }
                    }
                    return true
                }
            })
        }
    }

    override fun onBookClicked(view: View, book: Book) {
        findNavController().navigate(
            SearchFragmentDirections.actionSearchFragmentToBookDetailsFragment(
                book
            ),
            setTransitionStuff(
                getString(R.string.book_details_transition_name),
                view
            )
        )
    }

    // Set transition animation
    private fun setTransitionStuff(
        viewTransitionName: String,
        view: View
    ): FragmentNavigator.Extras {
        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }

        return FragmentNavigatorExtras(view to viewTransitionName)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}