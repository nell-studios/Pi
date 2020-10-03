package nellStudios.tech.pi.ui.fragments.Main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import dagger.hilt.android.AndroidEntryPoint
import nellStudios.tech.pi.R
import nellStudios.tech.pi.databinding.FragmentProfileBinding
import nellStudios.tech.pi.models.User
import nellStudios.tech.pi.ui.fragments.BaseFragment
import nellStudios.tech.pi.ui.fragments.MainBaseFragment

@AndroidEntryPoint
class ProfileFragment: MainBaseFragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun provideView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityBinding.titleText.text = getString(R.string.profile)
        user = User().apply {
            name = "Divyansh"
            email = "justdvnsh2208@gmail.com"
        }
        binding.user = user
    }
}