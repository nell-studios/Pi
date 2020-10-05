package nellStudios.tech.pi.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import nellStudios.tech.pi.databinding.ActivityMainBinding
import nellStudios.tech.pi.models.User
import nellStudios.tech.pi.ui.activities.MainActivity
import nellStudios.tech.pi.viewmodels.MainViewModel

abstract class MainBaseFragment: BaseFragment() {

    lateinit var activityViewModel: MainViewModel
    lateinit var user: User
    lateinit var activityBinding: ActivityMainBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityViewModel = (activity as MainActivity).viewModel
        activityBinding = (activity as MainActivity).binding
        activityBinding.mainSwipe.setOnRefreshListener { fetchUser() }
        fetchUser()
    }

    @SuppressLint("SetTextI18n")
    fun fetchUser() {
        activityBinding.mainSwipe.isRefreshing = true
        activityViewModel.getUser((activity as MainActivity).args.uid)
        activityViewModel.successfullGet.observe(viewLifecycleOwner, Observer {
            activityBinding.mainSwipe.isRefreshing = false
            user = it
            if (user.name == null) activityBinding.titleText.text = "Hi, ${user.phoneNumber}"
            else activityBinding.titleText.text = user.name
        })
    }
}