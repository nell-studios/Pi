package nellStudios.tech.pi.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import nellStudios.tech.pi.R
import nellStudios.tech.pi.databinding.ActivitySplashBinding
import nellStudios.tech.pi.viewmodels.SplashViewModel

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModels()
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkIfUserIsAuthenticated()
    }

    private fun checkIfUserIsAuthenticated() {
        viewModel.checkIfUserIsAuthenticated()
        viewModel.isUserAuthenticatedLiveData?.observe(this, Observer {
            if (it.phoneNumber == null) {
                Log.i("SPLASH", it.toString() + it.name)
            }
            if (!it.isAuthenticated!!) startActivity(Intent(this@SplashActivity, AuthActivity::class.java))
            else {
                Log.i("PHONE", it.phoneNumber!!)
                getUserFromDatabase(it.uid!!)
            }
        })
    }

    private fun getUserFromDatabase(uid: String) {
        viewModel.setPhoneNumber(uid)
        viewModel.userLiveData?.observe(this, Observer {
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            intent.putExtra("uid", uid)
            startActivity(intent)
            finish()
        })
    }
}