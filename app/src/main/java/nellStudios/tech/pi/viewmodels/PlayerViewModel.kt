package nellStudios.tech.pi.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nellStudios.tech.pi.models.User
import nellStudios.tech.pi.repositories.PlayerRepository

class PlayerViewModel @ViewModelInject constructor(
    private val repo: PlayerRepository
): ViewModel() {

    var successfull: LiveData<Boolean> = MutableLiveData()

    fun saveWatchedDuration(uid: String, topicName: String) {
        successfull = repo.saveWatchedVideoDuration(uid, topicName)
    }
}