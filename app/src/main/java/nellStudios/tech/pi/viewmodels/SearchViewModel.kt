package nellStudios.tech.pi.viewmodels

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nellStudios.tech.pi.models.Topic
import nellStudios.tech.pi.repositories.SearchRepository

class SearchViewModel @ViewModelInject constructor(
    private val repo: SearchRepository
): ViewModel() {

    var searchResult: LiveData<List<Topic>> = MutableLiveData()

    fun searchQuery(query: String) {
        searchResult = repo.searchForQuery(query)
        Log.i("SEARCH LD 2", searchResult.value.toString())
    }
}