package com.example.maximumhackathon

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class MainViewPagerAdapter(fragmentManager: FragmentManager, val context: Context) :
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val titles: List<String> = listOf(
        context.getString(R.string.title_tab_learning),
        context.getString(R.string.title_tab_tests),
        context.getString(R.string.title_tab_settings),
    )

    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
       return when (position) {
            0 -> {
                LearningFragment()
            }
            1 -> {
                TestsFragment()
            }
            2 -> {
                SettingsFragment()
            }
            else -> {
                throw RuntimeException()
            }

        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }

}