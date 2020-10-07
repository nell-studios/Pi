package nellStudios.tech.pi.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import nellStudios.tech.pi.models.Videos
import nellStudios.tech.pi.utils.Constants.Companion.TOPICS
import java.io.File
import javax.inject.Inject

class DownloadRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage
) {

    private val topicsRef = db.collection(TOPICS)

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
}