package com.example.maximumhackathon.presentation

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import com.example.maximumhackathon.R
import com.example.maximumhackathon.presentation.base.BaseFragment
import com.example.maximumhackathon.transaction
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_page_settings.*


class SettingsFragment: BaseFragment() {



    override fun getLayoutId(): Int {
        return R.layout.fragment_page_settings
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!Firebase.auth.currentUser?.providerData?.first()?.displayName.isNullOrEmpty()){
            if (Firebase.auth.currentUser?.providerData?.first()?.displayName?.isNotBlank() == true) {
                tvUserName.text = Firebase.auth.currentUser?.providerData?.first()?.displayName
            }
            tvUserEmail.text = Firebase.auth.currentUser?.providerData?.first()?.email
        }

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

        buttonChangeLevel.setOnClickListener {
            activity?.supportFragmentManager.transaction {
                add(
                    R.id.mainContainer,
                    ChangeLevelFragment(),
                    ChangeLevelFragment::class.java.name
                )
                addToBackStack(ChangeLevelFragment::class.java.name)
            }
        }
    }


}
