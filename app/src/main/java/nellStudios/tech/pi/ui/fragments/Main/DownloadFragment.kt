package nellStudios.tech.pi.ui.fragments.Main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_download.*
import nellStudios.tech.pi.R
import nellStudios.tech.pi.databinding.FragmentDownloadBinding
import nellStudios.tech.pi.ui.adapters.TopicDetailsAdapter
import nellStudios.tech.pi.ui.fragments.MainBaseFragment
import nellStudios.tech.pi.viewmodels.TopicDetailViewModel
import javax.inject.Inject

@AndroidEntryPoint
class DownloadFragment: MainBaseFragment() {

    private lateinit var binding: FragmentDownloadBinding
    private val viewModel: TopicDetailViewModel by viewModels()

    @Inject
    lateinit var topicDetailsAdapter: TopicDetailsAdapter

    override fun provideView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDownloadBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityViewModel.successfullGet.observe(viewLifecycleOwner, Observer {
            setupRecyclerView()
            setupItemTouchHelperCallback(view)
        })
    }

    private fun setupRecyclerView() {
        viewModel.getAllVideos(user.myLibrary)
        viewModel.videos.observe(viewLifecycleOwner, Observer {
            topicDetailsAdapter.differ.submitList(it)
        })
        topicDetailsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("video", it)
                putSerializable("user", user)
            }

            findNavController().navigate(R.id.action_topicDetailFragment_to_videoPlayerActivity, bundle)
        }
        topicDetailsAdapter.setOnItemDownloadListener {
            viewModel.downloadVideo(it)
            viewModel.downloaded.observe(viewLifecycleOwner, Observer {
                if (it) Snackbar.make(requireView(), "File Downloaded Successfully", Snackbar.LENGTH_SHORT).show()
                else Snackbar.make(requireView(), "File Download Failed", Snackbar.LENGTH_SHORT).show()
            })
        }
        binding.rvMyLibrary.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = topicDetailsAdapter
        }
    }

    private fun setupItemTouchHelperCallback(view: View) {
        val ItemTouchHelperCallback = object: ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val video = topicDetailsAdapter.differ.currentList[viewHolder.adapterPosition]
                viewModel.deleteVideo(video)
                Snackbar.make(view, "Removed From your library", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        viewModel.saveVideo(user.uid!!, video)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(ItemTouchHelperCallback).attachToRecyclerView(rvMyLibrary)
    }

    private fun setupObservers() {

    }
}