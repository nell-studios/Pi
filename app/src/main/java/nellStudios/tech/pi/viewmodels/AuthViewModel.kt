package nellStudios.tech.pi.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import nellStudios.tech.pi.models.User
import nellStudios.tech.pi.repositories.AuthRepository

class AuthViewModel @ViewModelInject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    var createdUserLiveData: LiveData<User>? = null

    fun createUser(user: User) {
        createdUserLiveData = authRepository.createUserInFirestoreIfNotExists(user)
    }
}