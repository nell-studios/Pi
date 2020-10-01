package nellStudios.tech.pi.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import nellStudios.tech.pi.models.User
import nellStudios.tech.pi.repositories.SplashRepository

class SplashViewModel @ViewModelInject constructor(
    private val splashRepository: SplashRepository
): ViewModel() {

    var isUserAuthenticatedLiveData: LiveData<User>? = null
    var userLiveData: LiveData<User>? = null

    fun checkIfUserIsAuthenticated() {
        isUserAuthenticatedLiveData = splashRepository.checkIfUserIsAuthenticatedInFirebase()
    }

    fun setPhoneNumber(uid: String) {
        userLiveData = splashRepository.addUserToLiveData(uid)
    }

}