package nellStudios.tech.pi.models

import java.io.Serializable

data class WatchedTopics(
    var watchedDuration: Long? = 0,
    var watchedPercentage: Long? = 0,
    var topic: String? = null
): Serializable