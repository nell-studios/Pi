package nellStudios.tech.pi.repositories

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import nellStudios.tech.pi.models.User
import nellStudios.tech.pi.utils.Constants.Companion.USERS
import javax.inject.Inject

class PlayerRepository @Inject constructor(
    private val db: FirebaseFirestore
) {

    private val usersRef = db.collection(USERS)

    fun saveWatchedVideoDuration(user: User): MutableLiveData<Boolean> {
        val saved: MutableLiveData<Boolean> = MutableLiveData()
        usersRef.document(user.uid!!).set(user).addOnCompleteListener {
            saved.value = it.isSuccessful
        }
        return saved
    }
}