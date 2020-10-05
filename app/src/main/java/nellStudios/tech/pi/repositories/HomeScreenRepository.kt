package nellStudios.tech.pi.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import nellStudios.tech.pi.models.Topic
import nellStudios.tech.pi.utils.Constants.Companion.TOPICS
import nellStudios.tech.pi.utils.Constants.Companion.USERS
import javax.inject.Inject

class HomeScreenRepository @Inject constructor(
    private val db: FirebaseFirestore
){

    private val usersRef = db.collection(USERS)
    private val topicsRef = db.collection(TOPICS)

    fun setTopic(topic: Topic) {
        topicsRef.document(topic.topicName!!).set(topic).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.i("HOME", "Set succesfully")
            }
        }
    }

    fun getAllTopics(): MutableLiveData<List<Topic>> {
        val topicsList: MutableLiveData<List<Topic>> = MutableLiveData()
        topicsRef.get().addOnCompleteListener {
            if (it.isSuccessful) {
                Log.i("HOME SCREEN", it.result?.documents.toString())
                topicsList.value = it.result?.toObjects(Topic::class.java)?.toList()
            }
        }
        return topicsList
    }
}