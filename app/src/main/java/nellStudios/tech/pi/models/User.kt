package nellStudios.tech.pi.models

import java.io.Serializable

data class User(
    var uid: String? = null,
    var name: String? = null,
    @SuppressWarnings
    var email: String? = null,
    var phoneNumber: String? = null,
    var profileImageUrl: String? = null,
    var isAuthenticated: Boolean? = null,
    var isNew: Boolean? = null,
    var isCreated: Boolean? = null,
    var watched: List<Videos>? = null,
    var downloaded: List<Videos>? = null
) : Serializable