package nellStudios.tech.pi.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.continue_watching_item_view.view.*
import kotlinx.android.synthetic.main.video_item_view.view.thumbnailImage
import nellStudios.tech.pi.R
import nellStudios.tech.pi.models.Topic
import nellStudios.tech.pi.models.Videos
import nellStudios.tech.pi.models.WatchedTopics
import javax.inject.Inject

class ContinueWatchingAdapter: RecyclerView.Adapter<ContinueWatchingAdapter.VideoViewHolder>() {

    @Inject
    lateinit var auth: FirebaseAuth

    inner class VideoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val diffCallback = object: DiffUtil.ItemCallback<Topic>() {
        override fun areItemsTheSame(oldItem: Topic, newItem: Topic): Boolean {
            return oldItem.topicName == newItem.topicName
        }

        override fun areContentsTheSame(oldItem: Topic, newItem: Topic): Boolean {
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
        val topic = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(topic.bannerImageUrl).into(thumbnailImage)
            watchedPercentage.progress = topic.progress?.find {
                it.userId.equals(auth.currentUser?.uid)
            }!!.watchedPercentage?.toInt()!!
            setOnClickListener {
                onItemClickListener?.let{
                    it(topic)
                }
            }
        }
    }

    private var onItemClickListener: ((Topic) -> Unit)? = null

    fun setOnItemClickListener(listener: (Topic) -> Unit) {
        onItemClickListener = listener
    }
}