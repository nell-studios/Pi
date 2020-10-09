package nellStudios.tech.pi.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import nellStudios.tech.pi.models.Videos
import nellStudios.tech.pi.utils.Constants.Companion.TOPICS
import nellStudios.tech.pi.utils.Constants.Companion.VIDEOS
import java.io.File
import javax.inject.Inject

class TopicDetailRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage
) {

    private val topicsRef = db.collection(TOPICS)
    private val videosRef = db.collection(VIDEOS)

    fun downloadFile(video: Videos): MutableLiveData<Boolean> {
        val downloaded: MutableLiveData<Boolean> = MutableLiveData()
        val storageRef = storage.getReference("Screenrecorder-2020-06-25-02-23-07-962.mp4")
        val localFile = File.createTempFile("PiVideo", ".mp4")
        storageRef.getFile(localFile).addOnSuccessListener {
            Log.i("DOWNLOADREPO", "downloaded at ${localFile.absolutePath}")
            downloaded.value = true
        }.addOnFailureListener {
            downloaded.value = false
        }
        return downloaded
    }

    fun getAllVideos(videos: List<String>): MutableLiveData<List<Videos>> {
        var successfullGet: MutableLiveData<List<Videos>> = MutableLiveData()
        val videosList: MutableList<Videos> = mutableListOf()
        videos.forEach {
            videosRef.document(it).get().addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.i("randomTopic", it.result.toString())
                    val video = it.result?.toObject(Videos::class.java)
                    Log.i("randomTopic", video.toString())
                    videosList.add(video!!)
                    successfullGet.value = videosList
                }
            }
        }
        return successfullGet
    }
}