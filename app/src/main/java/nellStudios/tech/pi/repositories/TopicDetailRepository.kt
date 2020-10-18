package nellStudios.tech.pi.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import nellStudios.tech.pi.models.User
import nellStudios.tech.pi.models.Videos
import nellStudios.tech.pi.utils.Constants.Companion.TOPICS
import nellStudios.tech.pi.utils.Constants.Companion.USERS
import nellStudios.tech.pi.utils.Constants.Companion.VIDEOS
import java.io.File
import javax.inject.Inject

class TopicDetailRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage
) {

    private val topicsRef = db.collection(TOPICS)
    private val videosRef = db.collection(VIDEOS)
    private val usersRef = db.collection(USERS)

    fun saveToMyLibrary(uid: String, video: Videos): MutableLiveData<Boolean> {
        val saved: MutableLiveData<Boolean> = MutableLiveData()
        usersRef.document(uid).get().addOnSuccessListener {
            val user = it.toObject(User::class.java)
            val myLibrary = user?.myLibrary
            val addToLibrary = mutableListOf<String>()
            if (myLibrary == null) {
                addToLibrary.add(video.id!!)
            } else {
                addToLibrary.addAll(myLibrary)
                if (!addToLibrary.contains(video.id!!)) {
                    addToLibrary.add(video.id!!)
                }
            }
            usersRef.document(uid).update("myLibrary", addToLibrary).addOnCompleteListener { saved.value = it.isSuccessful }
        }
        return saved
    }

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

    fun removeFromMyLibrary(video: Videos): MutableLiveData<Boolean> {
        val deleted: MutableLiveData<Boolean> = MutableLiveData()
        usersRef.document(FirebaseAuth.getInstance().currentUser!!.uid).update("myLibrary", FieldValue.arrayRemove(video.id))
            .addOnCompleteListener { deleted.value = it.isSuccessful }
        return deleted
    }

    fun addToContinueWatching(topicName: String, uid: String): MutableLiveData<Boolean> {
        val successFull: MutableLiveData<Boolean> = MutableLiveData()
        usersRef.document(uid).get().addOnSuccessListener {
            val user = it.toObject(User::class.java)
            val watched = mutableListOf<String>()
            if (user?.watched == null) {
                watched.add(topicName)
            } else {
                watched.addAll( user.watched!!)
                if (!watched.contains(topicName)) {
                    watched.add(topicName)
                }
            }
            usersRef.document(uid).update("watched", watched).addOnCompleteListener {
                successFull.value = it.isSuccessful
            }
        }
        return successFull
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