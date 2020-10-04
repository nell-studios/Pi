package nellStudios.tech.pi.viewmodels

import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nellStudios.tech.pi.models.User
import nellStudios.tech.pi.repositories.ProfileRepository

class ProfileViewModel @ViewModelInject constructor(
    private val repo: ProfileRepository
): ViewModel() {

    var successfullUpload: LiveData<Boolean> = MutableLiveData()

    fun uploadFile(filePath: Uri, user: User) {
        successfullUpload = repo.uploadImageToStorage(filePath, user)
    }
}