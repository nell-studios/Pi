package nellStudios.tech.pi.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_code_verification.*
import nellStudios.tech.pi.R
import nellStudios.tech.pi.ui.activities.MainActivity
import nellStudios.tech.pi.utils.Constants.Companion.PHONE_AUTH_TIMEOUT_DURATION
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class CodeVerificationFragment: BaseFragment() {

    @Inject
    lateinit var phoneAuthProvider: PhoneAuthProvider
    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private val args: CodeVerificationFragmentArgs by navArgs()

    private var mVerificationId: String = ""

    override fun provideView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_code_verification, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sendCode()
    }

    private fun sendCode() {
        phoneAuthProvider.verifyPhoneNumber(
            "+91" + args.mobileNumber,
            120,
            TimeUnit.SECONDS,
            requireActivity(),
            callbacks
        )
    }

    private val callbacks = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            val code = p0.smsCode

            code?.let {
                otp_code.setText(code)
                verfiyVerficationCode(code)
            }
        }

        override fun onVerificationFailed(p0: FirebaseException) = Toast.makeText(context, p0.message.toString(), Toast.LENGTH_SHORT).show()

        override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(p0, p1)
            mVerificationId = p0
        }
    }

    private fun verfiyVerficationCode(code: String) {
        val creds = PhoneAuthProvider.getCredential(mVerificationId, code)

        signInWithPhoneAuthCreds(creds)
    }

    private fun signInWithPhoneAuthCreds(creds: PhoneAuthCredential) {
        firebaseAuth.signInWithCredential(creds).addOnCompleteListener {
            if (it.isSuccessful) {
                startActivity(Intent(requireActivity(), MainActivity::class.java))
            } else Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }
}