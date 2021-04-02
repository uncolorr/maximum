package com.example.maximumhackathon.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.core.view.get
import com.example.maximumhackathon.R
import com.example.maximumhackathon.domain.model.Lesson
import com.example.maximumhackathon.presentation.base.BaseFragment
import com.example.maximumhackathon.transaction
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_change_level.*
import kotlinx.android.synthetic.main.fragment_page_settings.*


class SettingsFragment: BaseFragment() {

    private var settings: SharedPreferences? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_page_settings
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settings = activity?.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        val containsPrefLevel = settings?.contains(PREF_LEVEL) ?: false
        if(containsPrefLevel) {
            val level = settings?.getString(PREF_LEVEL, "") ?: ""
            tvLevel.text = "Уровень: $level"
        } else {
            tvLevel.text = "Уровень: Средний"
        }

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

            val fragment = ChangeLevelFragment()
            fragment.onLevelChangedListener = { level ->
                tvLevel.text = "Уровень: $level"
            }
            activity?.supportFragmentManager.transaction {
                add(
                    R.id.mainContainer,
                    fragment,
                    ChangeLevelFragment::class.java.name
                )
                addToBackStack(ChangeLevelFragment::class.java.name)
            }
        }
    }

    companion object {
        private const val APP_PREFERENCES = "app_preferences"
        private const val PREF_LEVEL = "level"
    }
}
