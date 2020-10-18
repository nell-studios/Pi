package nellStudios.tech.pi.ui.fragments.Main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import nellStudios.tech.pi.R
import nellStudios.tech.pi.databinding.FragmentHomeBinding
import nellStudios.tech.pi.models.Topic
import nellStudios.tech.pi.models.Videos
import nellStudios.tech.pi.ui.adapters.ContinueWatchingAdapter
import nellStudios.tech.pi.ui.adapters.ExploreTopicsAdapter
import nellStudios.tech.pi.ui.fragments.MainBaseFragment
import nellStudios.tech.pi.viewmodels.HomeScreenViewModel
import javax.inject.Inject

@AndroidEntryPoint
class HomeScreenFragment: MainBaseFragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeScreenViewModel by viewModels()

    @Inject
    lateinit var exploreTopicsAdapter: ExploreTopicsAdapter
    @Inject
    lateinit var continueWatchingAdapter: ContinueWatchingAdapter

    override fun provideView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupObservers()
        binding.apply {
            continueWatchingSeeMore.setOnClickListener { findNavController().navigate(R.id.action_homeScreenFragment_to_continueWatchingFragment) }
            exploreSeeMore.setOnClickListener { findNavController().navigate(R.id.action_homeScreenFragment_to_exploreDetailFragment) }
        }

//        val topic = Topic().apply {
//            size = 1
//            topicName = "RandomTopic"
//            videos = mutableListOf(
//                    "S3QC9xmZwElpNVFBtsXB")
//        }
//        viewModel.setTopic(topic)

        setupExploreRecyclerView()
    }

    private fun setupObservers() {
        activityViewModel.successfullGet.observe(viewLifecycleOwner, Observer {
            setupContinueWatchingReyclerView()
        })
    }

    private fun setupExploreRecyclerView() {
        viewModel.fetchTopicsList()
        viewModel.topicsList.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, "Fetched", Toast.LENGTH_SHORT).show()
            Log.i("HOME", it.toString())
            exploreTopicsAdapter.differ.submitList(it)
            exploreTopicsAdapter.setOnItemClickListener {
                val bundle = Bundle().apply {
                    putSerializable("topic", it)
                }
                findNavController().navigate(R.id.action_homeScreenFragment_to_topicDetailFragment, bundle)
            }
            rvExplore.apply {
                layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
                adapter = exploreTopicsAdapter
            }
        })
    }

    private fun setupContinueWatchingReyclerView() {
//        continueWatchingAdapter.setOnItemClickListener {
//            val bundle = Bundle().apply {
//                putSerializable("video", it.video)
//                putSerializable("user", user)
//            }
//            findNavController().navigate(R.id.action_homeScreenFragment_to_videoPlayerActivity, bundle)
//        }
        if (user.watched != null) {
            viewModel.getTopicbyUid(user.watched!!)
            viewModel.topicsList.observe(viewLifecycleOwner, Observer {
                continueWatchingAdapter.differ.submitList(it)
            })
        }
        rvContinueWatching.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = continueWatchingAdapter
        }
    }
}