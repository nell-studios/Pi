package nellStudios.tech.pi.models

import java.io.Serializable

data class Videos(
    var title: String? = null,
    var description: String? = null,
    var cuepoint: String? = null,
    var videoUrl: String? = null,
    var thumbnailUrl: String? = null,
    var topic: String? = null
): Serializable