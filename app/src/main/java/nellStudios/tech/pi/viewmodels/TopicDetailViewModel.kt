package nellStudios.tech.pi.viewmodels

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nellStudios.tech.pi.models.Videos
import nellStudios.tech.pi.repositories.TopicDetailRepository

class TopicDetailViewModel @ViewModelInject constructor(
    private val repo: TopicDetailRepository
): ViewModel() {

    var successfull: LiveData<Boolean> = MutableLiveData()
    var videos: LiveData<List<Videos>> = MutableLiveData()

    fun addToContinueWatching(topicName: String, uid: String) {
        successfull = repo.addToContinueWatching(topicName, uid)
    }

    fun getAllVideos(videosString: List<String>?) {
        videosString?.let {
            Log.i("randomTopic", it.toString())
            videos = repo.getAllVideos(videosString)
        }
    }
}