package nellStudios.tech.pi.ui.fragments.Main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import nellStudios.tech.pi.R
import nellStudios.tech.pi.databinding.FragmentSearchBinding
import nellStudios.tech.pi.ui.fragments.BaseFragment
import nellStudios.tech.pi.ui.fragments.MainBaseFragment

class SearchFragment: MainBaseFragment() {

    private lateinit var binding: FragmentSearchBinding

    override fun provideView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityBinding.titleText.text = getString(R.string.search)
    }
}