package nellStudios.tech.pi.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import nellStudios.tech.pi.models.Topic
import nellStudios.tech.pi.utils.Constants.Companion.TOPICS
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val db: FirebaseFirestore
) {

    private val topicsRef = db.collection(TOPICS)

    fun searchForQuery(query: String): MutableLiveData<List<Topic>> {
        val topicLiveData: MutableLiveData<List<Topic>> = MutableLiveData()

        topicsRef
            .whereEqualTo("topicName", query)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.i("SEARCH", it.result?.toObjects(Topic::class.java)?.toList().toString())
                    topicLiveData.value = it.result?.toObjects(Topic::class.java)?.toList()
                    Log.i("SEARCH LD", topicLiveData.value.toString())
                }
            }

        return topicLiveData
    }
}
