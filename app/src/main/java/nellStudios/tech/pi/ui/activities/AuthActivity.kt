package nellStudios.tech.pi.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import nellStudios.tech.pi.R
import nellStudios.tech.pi.databinding.ActivityAuthBinding
import nellStudios.tech.pi.viewmodels.AuthViewModel

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    val viewModel: AuthViewModel by viewModels()
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}