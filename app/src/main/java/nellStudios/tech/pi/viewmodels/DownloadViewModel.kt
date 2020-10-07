package nellStudios.tech.pi.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nellStudios.tech.pi.models.Videos
import nellStudios.tech.pi.repositories.DownloadRepository

class DownloadViewModel @ViewModelInject constructor(
    private val repo: DownloadRepository
): ViewModel() {

    var successfull: LiveData<Boolean> = MutableLiveData()

    fun download(video: Videos) {
        successfull = repo.downloadFile(video)
    }
}