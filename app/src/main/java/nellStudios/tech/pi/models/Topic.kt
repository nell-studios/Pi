package nellStudios.tech.pi.models

import android.widget.ProgressBar
import java.io.Serializable

data class Topic(
    var topicName: String? = null,
    var size: Int = 0,
    var bannerImageUrl: String = "https://firebasestorage.googleapis.com/v0/b/pi-maths.appspot.com/o/placeholder.png?alt=media&token=8766ba69-24ee-47ae-bb14-3efafdd2eb83",
    var videos: List<String>? = null,
    var progress: List<Progress>? = null
) : Serializable