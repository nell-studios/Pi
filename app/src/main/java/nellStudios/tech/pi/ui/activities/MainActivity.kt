package nellStudios.tech.pi.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.navigation.navArgs
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import nellStudios.tech.pi.R
import nellStudios.tech.pi.models.User

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var user: User
    private val args: MainActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupUser()
        bottomNavigationView.setupWithNavController(navHostFragment.findNavController())
    }

    private fun setupUser() { user = args.user }
}