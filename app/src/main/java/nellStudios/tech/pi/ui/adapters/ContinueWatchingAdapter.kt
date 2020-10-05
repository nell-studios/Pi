package nellStudios.tech.pi.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.continue_watching_item_view.view.*
import kotlinx.android.synthetic.main.video_item_view.view.*
import kotlinx.android.synthetic.main.video_item_view.view.thumbnailImage
import nellStudios.tech.pi.R
import nellStudios.tech.pi.models.WatchedVideos

class ContinueWatchingAdapter: RecyclerView.Adapter<ContinueWatchingAdapter.VideoViewHolder>() {
    inner class VideoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val diffCallback = object: DiffUtil.ItemCallback<WatchedVideos>() {
        override fun areItemsTheSame(oldItem: WatchedVideos, newItem: WatchedVideos): Boolean {
            return oldItem.video?.videoUrl == newItem.video?.videoUrl
        }

        override fun areContentsTheSame(oldItem: WatchedVideos, newItem: WatchedVideos): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        return VideoViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.continue_watching_item_view, parent, false)
        )
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(video.video?.thumbnailUrl).into(thumbnailImage)
            watchedPercentage.progress = video.watchedDuration?.toInt()!!
            setOnClickListener {
                onItemClickListener?.let{
                    it(video)
                }
            }
        }
    }

    private var onItemClickListener: ((WatchedVideos) -> Unit)? = null

    fun setOnItemClickListener(listener: (WatchedVideos) -> Unit) {
        onItemClickListener = listener
    }
}