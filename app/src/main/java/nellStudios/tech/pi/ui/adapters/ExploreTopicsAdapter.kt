package nellStudios.tech.pi.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.topic_item_view.view.*
import nellStudios.tech.pi.R
import nellStudios.tech.pi.models.Topic

class ExploreTopicsAdapter: RecyclerView.Adapter<ExploreTopicsAdapter.TopicsViewHolder>() {
    inner class TopicsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val diffCallback = object: DiffUtil.ItemCallback<Topic>() {
        override fun areItemsTheSame(oldItem: Topic, newItem: Topic): Boolean {
            return oldItem.topicName == newItem.topicName
        }

        override fun areContentsTheSame(oldItem: Topic, newItem: Topic): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicsViewHolder {
        return TopicsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.topic_item_view, parent, false)
        )
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: TopicsViewHolder, position: Int) {
        val topic = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(topic.bannerImageUrl).into(backgroundImage)
            topicName.text = topic.topicName
            topicContents.text = topic.size.toString()
        }
    }
}