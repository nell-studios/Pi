package nellStudios.tech.pi.ui.fragments

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.common_toolbar.*
import nellStudios.tech.pi.models.User
import nellStudios.tech.pi.ui.activities.MainActivity

abstract class MainBaseFragment: BaseFragment() {

    lateinit var user: User

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user = (activity as MainActivity).user
    }

    fun setupTitle(text: String) {
        titleText?.text = text
    }
}