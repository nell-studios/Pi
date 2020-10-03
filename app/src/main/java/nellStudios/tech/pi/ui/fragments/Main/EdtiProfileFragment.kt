package nellStudios.tech.pi.ui.fragments.Main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import nellStudios.tech.pi.R
import nellStudios.tech.pi.databinding.FragmentEditProfileBinding
import nellStudios.tech.pi.ui.fragments.BaseFragment
import nellStudios.tech.pi.ui.fragments.MainBaseFragment
import nellStudios.tech.pi.viewmodels.EditProfileViewModel

@AndroidEntryPoint
class EdtiProfileFragment: MainBaseFragment() {

    private val args: EdtiProfileFragmentArgs by navArgs()
    private lateinit var binding: FragmentEditProfileBinding

    private val viewModel: EditProfileViewModel by viewModels()

    override fun provideView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.user = args.user
        binding.saveBtn.setOnClickListener { saveDetails() }
    }

    private fun saveDetails() {
        val newUser = args.user.copy(
            name = name.text.toString(),
            email = email.text.toString(),
            phoneNumber = mobile_number_edit_profile.text.toString()
        )
        viewModel.updateDetails(newUser)
        viewModel.successfullUpdate.observe(viewLifecycleOwner, Observer {
            if (it) Snackbar.make(requireView(), "Updated Successfully", Snackbar.LENGTH_SHORT).show()
            else Snackbar.make(requireView(), "Updated Failed", Snackbar.LENGTH_SHORT).show()
            findNavController().popBackStack()
        })
    }
}