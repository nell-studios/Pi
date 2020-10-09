package nellStudios.tech.pi.models

import java.io.Serializable

data class User(
    var uid: String? = null,
    var name: String? = null,
    @SuppressWarnings
    var email: String? = null,
    var phoneNumber: String? = null,
    var profileImageUrl: String = "https://firebasestorage.googleapis.com/v0/b/pi-maths.appspot.com/o/male-placeholder-image.jpeg?alt=media&token=10adc938-a747-4f03-afbf-99fa82cc73da",
    var isAuthenticated: Boolean? = null,
    var isNew: Boolean? = null,
    var isCreated: Boolean? = null,
    var watched: MutableList<String>? = null,
    var downloaded: MutableList<String>? = null,
    var myLibrary: MutableList<String>? = null
) : Serializable