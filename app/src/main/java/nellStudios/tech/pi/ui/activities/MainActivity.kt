package nellStudios.tech.pi.ui.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navArgs
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import nellStudios.tech.pi.R
import nellStudios.tech.pi.databinding.ActivityMainBinding
import nellStudios.tech.pi.models.User
import nellStudios.tech.pi.viewmodels.MainViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    var user: User = User()
    private val args: MainActivityArgs by navArgs()
    lateinit var binding: ActivityMainBinding
    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        fetchUser()
        binding.mainSwipe.setOnRefreshListener { fetchUser() }
        bottomNavigationView.setupWithNavController(navHostFragment.findNavController())
    }

    @SuppressLint("SetTextI18n")
    fun fetchUser() {
        binding.mainSwipe.isRefreshing = true
        viewModel.getUser(args.uid)
        viewModel.successfullGet.observe(this, Observer {
            binding.mainSwipe.isRefreshing = false
            user = it
        })
    }
}