package nellStudios.tech.pi.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import nellStudios.tech.pi.models.User
import nellStudios.tech.pi.utils.Constants.Companion.USERS
import javax.inject.Inject

class SplashRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) {

    private val usersRef = db.collection(USERS)
    private val user: User = User()

    fun checkIfUserIsAuthenticatedInFirebase(): MutableLiveData<User> {
        val isUserAuthenticatedInFirebaseLiveData: MutableLiveData<User> = MutableLiveData()
        val firebaseUser = auth.currentUser
        if (firebaseUser == null) {
            user.isAuthenticated  = false
            isUserAuthenticatedInFirebaseLiveData.value = user
        } else {
            val uidRef: DocumentReference = usersRef.document(firebaseUser.uid)
            uidRef.get().addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    user.uid = task.result?.get("uid").toString()
                    user.phoneNumber = task.result?.get("phoneNumber").toString()
                    user.isAuthenticated = true
                    user.isNew = false
                    isUserAuthenticatedInFirebaseLiveData.value = user
                } else {
                    Log.i("AUTHREPO CREATING USER", "FAILED ${task.exception?.message}")
                }
            }
        }

        return isUserAuthenticatedInFirebaseLiveData
    }

    fun addUserToLiveData(uid: String): MutableLiveData<User> {
        val userMutableLiveData: MutableLiveData<User> = MutableLiveData()
        usersRef.document(uid).get().addOnCompleteListener {
            if (it.isSuccessful) {
                val document = it.result
                if (document?.exists()!!) {
                    val user: User = User(
                        uid = document.get("uid") as String,
                        phoneNumber = document.get("phoneNumber") as String
                    )
                    userMutableLiveData.value = user
                }
            } else {
                Log.i("SPLASHREPO", it.exception?.message)
            }
        }

        return userMutableLiveData
    }
}