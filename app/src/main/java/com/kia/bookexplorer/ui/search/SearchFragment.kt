package com.kia.bookexplorer.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.kia.bookexplorer.R
import com.kia.bookexplorer.databinding.FragmentSearchBinding
import com.kia.bookexplorer.ui.search.adapter.BookAdapter
import com.kia.bookexplorer.ui.search.adapter.BookListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(), BookListener {

    val viewModel by viewModels<SearchViewModel>()
    private var binding : FragmentSearchBinding? = null

    val bookAdapter = BookAdapter(this)

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

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.book.collectLatest {
                if (it != null) {
                    bookAdapter.submitData(it)
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
        }
    }


    override fun onBookClicked(position: Int, bookTitle: String?) {
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}