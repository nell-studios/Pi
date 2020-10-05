package nellStudios.tech.pi.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.video_item_view.view.*
import nellStudios.tech.pi.R
import nellStudios.tech.pi.models.Videos

class TopicDetailsAdapter: RecyclerView.Adapter<TopicDetailsAdapter.VideoViewHolder>() {
    inner class VideoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val diffCallback = object: DiffUtil.ItemCallback<Videos>() {
        override fun areItemsTheSame(oldItem: Videos, newItem: Videos): Boolean {
            return oldItem.videoUrl == newItem.videoUrl
        }

        override fun areContentsTheSame(oldItem: Videos, newItem: Videos): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        return VideoViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.video_item_view, parent, false)
        )
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(video.thumbnailUrl).into(thumbnailImage)
            videoTitle.text = video.title
            videoDescription.text = video.description
            setOnClickListener {
                onItemClickListener?.let{
                    it(video)
                }
            }
        }
    }

    private var onItemClickListener: ((Videos) -> Unit)? = null

    fun setOnItemClickListener(listener: (Videos) -> Unit) {
        onItemClickListener = listener
    }
}