package nellStudios.tech.pi.repositories

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import nellStudios.tech.pi.models.User
import nellStudios.tech.pi.utils.Constants.Companion.USERS
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val storage: FirebaseStorage,
    private val db: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) {

    private val usersRef = db.collection(USERS)

    fun uploadImageToStorage(filePath: Uri, user: User): MutableLiveData<Boolean> {
        val uploaded: MutableLiveData<Boolean> = MutableLiveData()
        val storageRef = storage.getReference("userImage/${user.uid}")
        storageRef.putFile(filePath).addOnSuccessListener {
            storage.getReference("userImage/${user.uid}").downloadUrl.addOnSuccessListener {
                val modifiedUser = user.copy(profileImageUrl = it.toString())
                usersRef.document(firebaseAuth.currentUser?.uid!!).set(modifiedUser).addOnCompleteListener {
                    uploaded.value = it.isSuccessful
                }
            }.addOnFailureListener {
                uploaded.value = false
            }
        }.addOnFailureListener { uploaded.value = false }
        return uploaded
    }
}