package nellStudios.tech.pi.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import nellStudios.tech.pi.models.User
import nellStudios.tech.pi.utils.Constants.Companion.USERS
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) {

    private val usersRef = db.collection(USERS)

    fun createUserInFirestoreIfNotExists(user: User): MutableLiveData<User> {
        val newUserMutableLiveData: MutableLiveData<User> = MutableLiveData()
        val uidRef: DocumentReference = usersRef.document(user.uid!!)
        uidRef.get().addOnCompleteListener {task ->
            if (task.isSuccessful) {
                val document = task.result
                if (!document?.exists()!!) {
                    user.isNew = true
                    uidRef.set(user).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            user.isCreated = true
                            newUserMutableLiveData.value = user
                        } else {
                            Log.i("AUTHREPO SETTING", task.exception?.message)
                        }
                    }
                } else {
                    newUserMutableLiveData.value = user
                }
            } else {
                Log.i("AUTHREPO CREATING USER", "FAILED ${task.exception?.message}")
            }
        }

        return newUserMutableLiveData
    }
}