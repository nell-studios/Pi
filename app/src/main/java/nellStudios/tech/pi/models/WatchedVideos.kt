package nellStudios.tech.pi.models

import java.io.Serializable

data class WatchedVideos(
    val watchedDuration: Long? = 0,
    val watchedPercentage: Long? = 0,
    val videoUrl: String? = null
): Serializable