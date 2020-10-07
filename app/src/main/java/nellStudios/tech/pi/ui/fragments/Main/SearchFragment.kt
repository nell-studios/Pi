package nellStudios.tech.pi.ui.fragments.Main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import nellStudios.tech.pi.R
import nellStudios.tech.pi.databinding.FragmentSearchBinding
import nellStudios.tech.pi.ui.adapters.ExploreTopicsAdapter
import nellStudios.tech.pi.ui.fragments.BaseFragment
import nellStudios.tech.pi.ui.fragments.MainBaseFragment
import nellStudios.tech.pi.utils.Constants.Companion.SEARCH_TIME_DELAY
import nellStudios.tech.pi.viewmodels.SearchViewModel
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment: MainBaseFragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()

    @Inject
    lateinit var topicsAdapter: ExploreTopicsAdapter

    override fun provideView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityBinding.titleText.text = getString(R.string.search)
        setupRecyclerView()
        setupEditText()
    }

    private fun setupEditText() {
        var job: Job? = null
        searchEditText.addTextChangedListener {
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_TIME_DELAY)
                it?.let {
                    if (it.toString().isNotEmpty()) {
                        viewModel.searchQuery(it.toString())
                        setupObservers()
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        topicsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("topic", it)
            }
            findNavController().navigate(R.id.action_searchFragment_to_topicDetailFragment)
        }
        searchRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = topicsAdapter
        }
    }

    private fun setupObservers() {
        viewModel.searchResult.observe(viewLifecycleOwner, Observer {
            Log.i("SEARCH", "Inside Observer")
            topicsAdapter.differ.submitList(it)
        })
    }
}