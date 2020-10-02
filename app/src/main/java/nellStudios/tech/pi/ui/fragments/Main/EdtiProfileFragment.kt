package nellStudios.tech.pi.ui.fragments.Main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import nellStudios.tech.pi.R
import nellStudios.tech.pi.ui.fragments.BaseFragment
import nellStudios.tech.pi.ui.fragments.MainBaseFragment

class EdtiProfileFragment: MainBaseFragment() {
    override fun provideView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_edit_profile, container, false)
}