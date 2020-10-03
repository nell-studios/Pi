package nellStudios.tech.pi.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import nellStudios.tech.pi.models.User
import nellStudios.tech.pi.utils.Constants.Companion.USERS
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val db: FirebaseFirestore
) {

    private val usersRef = db.collection(USERS)

    fun fetchUserDetails(uid: String): MutableLiveData<User> {
        val userLiveData: MutableLiveData<User> = MutableLiveData()
        val uidRef: DocumentReference = usersRef.document(uid)
        userLiveData.value = User()
        uidRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = task.result?.toObject(User::class.java)
                userLiveData.value = user
            } else Log.i("AUTHREPO CREATING USER", "FAILED ${task.exception?.message}")
        }

        return userLiveData
    }
}