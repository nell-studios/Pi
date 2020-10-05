package nellStudios.tech.pi.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nellStudios.tech.pi.models.Topic
import nellStudios.tech.pi.repositories.HomeScreenRepository

class HomeScreenViewModel @ViewModelInject constructor(
    private val repo: HomeScreenRepository
): ViewModel() {

    var topicsList: LiveData<List<Topic>> = MutableLiveData()

    fun fetchTopicsList() {
        topicsList = repo.getAllTopics()
    }

    // delete this code - Only for testing
    fun setTopic(topic: Topic) = repo.setTopic(topic)
}