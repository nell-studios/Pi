package nellStudios.tech.pi.ui.fragments.Main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_topic_detail.*
import nellStudios.tech.pi.R
import nellStudios.tech.pi.ui.adapters.TopicDetailsAdapter
import nellStudios.tech.pi.ui.fragments.MainBaseFragment
import nellStudios.tech.pi.viewmodels.TopicDetailViewModel
import javax.inject.Inject

@AndroidEntryPoint
class TopicDetailFragment: MainBaseFragment() {

    private val args: TopicDetailFragmentArgs by navArgs()
    private val viewModel: TopicDetailViewModel by viewModels()

    @Inject
    lateinit var topicDetailsAdapter: TopicDetailsAdapter

    override fun provideView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_topic_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        viewModel.getAllVideos(args.topic.videos)
        viewModel.videos.observe(viewLifecycleOwner, Observer {
            topicDetailsAdapter.differ.submitList(it)
        })
        topicDetailsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("video", it)
                putSerializable("user", user)
            }

            viewModel.addToContinueWatching(args.topic.topicName!!, user.uid!!)
            viewModel.successfull.observe(viewLifecycleOwner, Observer {
                if (it) findNavController().navigate(R.id.action_topicDetailFragment_to_videoPlayerActivity, bundle)
            })

        }
        topicDetailsAdapter.setOnDownloadClickListener {

        }
        rvTopicVideos.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = topicDetailsAdapter
        }
    }
}