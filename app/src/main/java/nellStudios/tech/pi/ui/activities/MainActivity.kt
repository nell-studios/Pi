package nellStudios.tech.pi.ui.activities

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navArgs
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import nellStudios.tech.pi.R
import nellStudios.tech.pi.databinding.ActivityMainBinding
import nellStudios.tech.pi.models.User
import nellStudios.tech.pi.ui.fragments.Main.DownloadFragment
import nellStudios.tech.pi.ui.fragments.Main.HomeScreenFragment
import nellStudios.tech.pi.ui.fragments.Main.ProfileFragment
import nellStudios.tech.pi.ui.fragments.Main.SearchFragment
import nellStudios.tech.pi.viewmodels.MainViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity(){

    var user: User = User()
    val args: MainActivityArgs by navArgs()
    lateinit var binding: ActivityMainBinding
    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        bottomNavigationView.setupWithNavController(navHostFragment.findNavController())
        val profileItem = bottomNavigationView.menu.findItem(R.id.profileFragment2)
        Glide.with(this)
            .asBitmap()
            .load(user.profileImageUrl)
            .apply(
                RequestOptions
                .circleCropTransform()
                .placeholder(R.drawable.ic_baseline_person_24))
            .into(object: CustomTarget<Bitmap>(24, 24) {
                override fun onLoadCleared(placeholder: Drawable?) {}

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    profileItem?.icon = BitmapDrawable(resources, resource)
                }
            })
    }

}