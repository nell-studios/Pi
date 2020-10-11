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
import com.bumptech.glide.Glide
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
        setupView()
    }

    private fun setupView() {
        Glide.with(requireContext()).load(args.topic.bannerImageUrl).into(topicBanner)
        topicName.text = args.topic.topicName
        topicContents.text = "${args.topic.size} videos"
        back.setOnClickListener { findNavController().popBackStack() }
        activityBinding.bottomNavigationView.visibility = View.GONE
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

        topicDetailsAdapter.setOnItemDownloadListener {
            viewModel.downloadVideo(it)
            viewModel.downloaded.observe(viewLifecycleOwner, Observer {
                if (it) Snackbar.make(requireView(), "File Downloaded Successfully", Snackbar.LENGTH_SHORT).show()
                else Snackbar.make(requireView(), "File Download Failed", Snackbar.LENGTH_SHORT).show()
            })
        }

        topicDetailsAdapter.setOnItemSaveListener {
            viewModel.saveVideo(user.uid!!, it)
            viewModel.saved.observe(viewLifecycleOwner, Observer {
                if (it) Snackbar.make(requireView(), "Video Saved Successfully", Snackbar.LENGTH_SHORT).show()
                else Snackbar.make(requireView(), "Failed", Snackbar.LENGTH_SHORT).show()
            })
        }

        rvTopicVideos.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = topicDetailsAdapter
        }
    }

    override fun onStop() {
        super.onStop()
        activityBinding.bottomNavigationView.visibility = View.VISIBLE
    }
}