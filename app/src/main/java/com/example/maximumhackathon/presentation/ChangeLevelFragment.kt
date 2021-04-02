package com.example.maximumhackathon.presentation

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.maximumhackathon.R
import com.example.maximumhackathon.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_change_level.*

class ChangeLevelFragment: BaseFragment() {

    private var settings: SharedPreferences? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_change_level
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()

        settings = activity?.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        val editor = settings?.edit()
        editor?.putString(PREF_LEVEL, "")
        editor?.apply()
        radioGroupLevel.check(R.id.radioButtonMediumLevel)

        buttonSave.setOnClickListener {
            when(radioGroupLevel.checkedRadioButtonId) {
                R.id.radioButtonLowLevel -> {

                }
                R.id.radioButtonMediumLevel -> {

                }
                R.id.radioButtonHighLevel -> {

                }
            }
        }
    }

    private fun initToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    companion object {
        private const val APP_PREFERENCES = "app_preferences"
        private const val PREF_LEVEL = "level"
    }
}