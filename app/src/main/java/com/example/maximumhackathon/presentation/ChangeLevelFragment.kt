package com.example.maximumhackathon.presentation

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.example.maximumhackathon.R
import com.example.maximumhackathon.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_change_level.*

class ChangeLevelFragment: BaseFragment() {

    var onLevelChangedListener: ((String) -> Unit)? = null

    private var settings: SharedPreferences? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_change_level
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()

        settings = activity?.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        val editor = settings?.edit()

        val containsPrefLevel = settings?.contains(PREF_LEVEL) ?: false
        if(!containsPrefLevel) {
            radioGroupLevel.check(R.id.radioButtonMediumLevel)
        } else {
            val prefLevel = settings?.getString(PREF_LEVEL, "")
            for (i in 0 until radioGroupLevel.childCount) {
                val button = radioGroupLevel[i] as RadioButton
                if(button.text == prefLevel) {
                    radioGroupLevel.check(button.id)
                }
            }
        }

        buttonSave.setOnClickListener {
            val radioButtonID: Int = radioGroupLevel.checkedRadioButtonId
            val checkedButton: RadioButton = radioGroupLevel.findViewById(radioButtonID)
            val level = checkedButton.text.toString()
            editor?.putString(PREF_LEVEL, level)
            editor?.apply()
            onLevelChangedListener?.invoke(level)
            activity?.supportFragmentManager?.popBackStack()
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