package nellStudios.tech.pi.repositories

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import nellStudios.tech.pi.models.Progress
import nellStudios.tech.pi.models.Topic
import nellStudios.tech.pi.models.User
import nellStudios.tech.pi.utils.Constants.Companion.TOPICS
import nellStudios.tech.pi.utils.Constants.Companion.USERS
import javax.inject.Inject

class PlayerRepository @Inject constructor(
    private val db: FirebaseFirestore
) {

    private val topicsRef = db.collection(TOPICS)

    fun saveWatchedVideoDuration(uid: String, topicName: String): MutableLiveData<Boolean> {
        val saved: MutableLiveData<Boolean> = MutableLiveData()
        topicsRef.document(topicName).get().addOnSuccessListener {
            val topic = it.toObject(Topic::class.java)
            val progress = Progress()
            topic?.progress.let {
                if (it == null) {
                    progress.copy(userId = uid,
                    watchedVideos = 1)
                } else {
                    val count = it.find { it.userId.equals(uid) }!!
                    progress.copy(userId = count.userId, watchedVideos = count.watchedVideos!! + 1)
                }
            }
            topicsRef.document(topicName).update("progress",progress).addOnCompleteListener {
                saved.value = it.isSuccessful
            }
        }
        return saved
    }
}