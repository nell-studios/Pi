package nellStudios.tech.pi.ui.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.navArgs
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import nellStudios.tech.pi.R
import nellStudios.tech.pi.databinding.ActivityMainBinding
import nellStudios.tech.pi.models.User

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var user: User
    private val args: MainActivityArgs by navArgs()
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupUser()
        bottomNavigationView.setupWithNavController(navHostFragment.findNavController())
    }

    @SuppressLint("SetTextI18n")
    private fun setupUser() {
        user = args.user
    }
}