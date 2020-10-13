package nellStudios.tech.pi.models

import java.io.Serializable

data class Videos(
    var id: String? = null,
    var title: String? = null,
    var description: String? = null,
    var videoUrl: String? = null,
    var thumbnailUrl: String = "https://firebasestorage.googleapis.com/v0/b/pi-maths.appspot.com/o/placeholder.png?alt=media&token=8766ba69-24ee-47ae-bb14-3efafdd2eb83",
    var topicName: String? = null,
    var videoNumber: Int? = null,
    var nextVideo: String? = null,
    var previousVideo: String? = null
): Serializable