package nellStudios.tech.pi.ui.fragments.Main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile.*
import nellStudios.tech.pi.R
import nellStudios.tech.pi.databinding.FragmentProfileBinding
import nellStudios.tech.pi.models.User
import nellStudios.tech.pi.ui.activities.MainActivity
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
        (activity as MainActivity).fetchUser()
        Glide.with(this).load(user.profileImageUrl).into(profileImage)
        binding.user = user
    }
}