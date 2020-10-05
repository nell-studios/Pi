package nellStudios.tech.pi.ui.fragments.Main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_topic_detail.*
import nellStudios.tech.pi.R
import nellStudios.tech.pi.ui.adapters.TopicDetailsAdapter
import nellStudios.tech.pi.ui.fragments.MainBaseFragment
import javax.inject.Inject

@AndroidEntryPoint
class TopicDetailFragment: MainBaseFragment() {

    private val args: TopicDetailFragmentArgs by navArgs()

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
        activityBinding.titleText.text = args.topic.topicName
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        topicDetailsAdapter.differ.submitList(args.topic.videos)
        topicDetailsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("video", it)
                putSerializable("user", user)
            }

            findNavController().navigate(R.id.action_topicDetailFragment_to_videoPlayerActivity, bundle)
        }
        rvTopicVideos.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = topicDetailsAdapter
        }
    }
}