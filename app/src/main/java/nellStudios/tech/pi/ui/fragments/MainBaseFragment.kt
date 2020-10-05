package nellStudios.tech.pi.ui.fragments

import android.os.Bundle
import android.view.View
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
        user = (activity as MainActivity).user
        activityViewModel = (activity as MainActivity).viewModel
        activityBinding = (activity as MainActivity).binding
    }
}