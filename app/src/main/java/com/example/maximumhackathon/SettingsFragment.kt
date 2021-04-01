package com.example.maximumhackathon

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.fragment_page_settings.*


class SettingsFragment: BaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_page_settings
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonExit.setOnClickListener {
            context?.let { context ->
                AuthUI.getInstance()
                    .signOut(context)
                    .addOnCompleteListener {
                        requireActivity().supportFragmentManager.transaction {
                            replace(
                                R.id.mainContainer,
                                MainFragment(),
                                MainFragment::class.java.name
                            )
                        }
                    }
            }
        }
    }
}
