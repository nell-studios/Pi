package nellStudios.tech.pi.ui.fragments.Main

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile.*
import nellStudios.tech.pi.R
import nellStudios.tech.pi.databinding.FragmentProfileBinding
import nellStudios.tech.pi.models.User
import nellStudios.tech.pi.ui.activities.MainActivity
import nellStudios.tech.pi.ui.fragments.BaseFragment
import nellStudios.tech.pi.ui.fragments.MainBaseFragment
import nellStudios.tech.pi.utils.Constants.Companion.PICK_IMAGE_REQUEST
import nellStudios.tech.pi.viewmodels.ProfileViewModel
import java.io.IOException

@AndroidEntryPoint
class ProfileFragment: MainBaseFragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModels()

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
        fetchUser()
        activityViewModel.successfullGet.observe(viewLifecycleOwner, Observer {
            Glide.with(this).load(user.profileImageUrl).into(profileImage)
            binding.user = user
            binding.editProfile.setOnClickListener {
                val bundle = Bundle().apply {
                    putSerializable(getString(R.string.userArgument), user)
                }
                findNavController().navigate(R.id.action_profileFragment2_to_edtiProfileFragment, bundle)
            }
            binding.profileImage.setOnClickListener {selectImage()}
        })
    }

    private fun selectImage() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(Intent.createChooser(intent, "Select your profile image"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            val filePath = data.data
            filePath?.let {
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, filePath)
                    binding.profileImage.setImageBitmap(bitmap)
                    Snackbar.make(requireView(), "Uploading image", Snackbar.LENGTH_SHORT).show()
                    viewModel.uploadFile(filePath, user)
                    viewModel.successfullUpload.observe(viewLifecycleOwner, Observer {
                        if (it) {
                            Snackbar.make(requireView(), "File Uploaded", Snackbar.LENGTH_SHORT).show()
                            fetchUser()
                        }
                        else Snackbar.make(requireView(), "Some Error Occured", Snackbar.LENGTH_SHORT).show()
                    })
                } catch (e: IOException) {e.printStackTrace()}
            }
        }
    }
}