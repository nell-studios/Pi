package nellStudios.tech.pi.ui.fragments.Auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_phone_number.*
import nellStudios.tech.pi.R
import nellStudios.tech.pi.ui.fragments.BaseFragment
import nellStudios.tech.pi.utils.Validators.Companion.isValid

class PhoneNumberFragment: BaseFragment() {

    override fun provideView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_phone_number, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        send_code.setOnClickListener { sendCode() }
    }

    private fun sendCode() {
        val mobileNumber = mobile_number.text.toString()
        if (isValid(mobileNumber)) {
            val bundle = Bundle().apply { putString(getString(R.string.mobile_number), mobileNumber) }
            findNavController().navigate(
                R.id.action_phoneNumberFragment_to_codeVerificationFragment,
                bundle
            )
        } else Toast.makeText(context, "Enter a valid mobile Number !", Toast.LENGTH_SHORT).show()
    }
}