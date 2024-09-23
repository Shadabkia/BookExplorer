package com.kia.bookexplorer.ui.book_details

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.android.material.transition.MaterialContainerTransform
import com.kia.bookexplorer.R
import com.kia.bookexplorer.databinding.FragmentBookDetailsBinding
import com.kia.bookexplorer.ui.search.adapter.BookListener
import com.kia.bookexplorer.utils.themeColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookDetailsFragment : Fragment() {

    private val viewModel by viewModels<BookDetailsViewModel>()
    private var binding: FragmentBookDetailsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Show transition animation
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.fragmentContainerView2
            duration = 300L
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(requireContext().themeColor(com.google.android.material.R.attr.colorSurface))
        }
    }


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
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.book.collectLatest {
                    tvBookTitle.text = it?.title
                    tvAuthors.text = it?.authorName?.fold("") { acc, s -> "$acc\n$s" }
                    ivCover.load("https://covers.openlibrary.org/b/id/${it?.coverI}-M.jpg") {
                        crossfade(true)
                        placeholder(R.drawable.hard_cover_book)
                    }

                    tvSubjects.text = it?.subject?.fold("") { acc, s -> "$acc, $s" }?.substring(2)

                    tvPublishers.text = it?.publisher?.fold("") { acc, s -> "$acc\n$s" }

                    tvPublishYears.text = it?.publishYear?.fold("") { acc, s -> "$acc\n$s" }

                    tvPublishPlaces.text = it?.publishPlace?.fold("") { acc, s -> "$acc\n$s" }


                }
            }
        }

    }

    private fun initListeners() {
        binding?.apply {
            ivBack.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}