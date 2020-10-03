package nellStudios.tech.pi.ui.fragments.Main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import nellStudios.tech.pi.R
import nellStudios.tech.pi.databinding.FragmentHomeBinding
import nellStudios.tech.pi.ui.fragments.BaseFragment
import nellStudios.tech.pi.ui.fragments.MainBaseFragment

class HomeScreenFragment: MainBaseFragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun provideView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (user.name == null) activityBinding.titleText.text = "Hi, ${user.phoneNumber}"
        else activityBinding.titleText.text = user.name
    }
}