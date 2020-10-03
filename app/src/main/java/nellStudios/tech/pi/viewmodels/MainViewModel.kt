package nellStudios.tech.pi.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nellStudios.tech.pi.models.User
import nellStudios.tech.pi.repositories.MainRepository

class MainViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository
): ViewModel() {

    var successfullGet: LiveData<User> = MutableLiveData()
    var user: User = User()

    fun getUser(uid: String) {
        successfullGet = mainRepository.fetchUserDetails(uid)
    }
}