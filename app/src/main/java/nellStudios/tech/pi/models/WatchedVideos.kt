package nellStudios.tech.pi.models

import java.io.Serializable

data class WatchedVideos(
    var watchedDuration: Long? = 0,
    var watchedPercentage: Long? = 0,
    var video: Videos? = null
): Serializable