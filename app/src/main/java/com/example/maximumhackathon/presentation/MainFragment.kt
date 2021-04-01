package com.example.maximumhackathon.presentation

import android.os.Bundle
import android.view.View
import com.example.maximumhackathon.R
import com.example.maximumhackathon.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment: BaseFragment() {

    private lateinit var pagerAdapter: MainViewPagerAdapter

    override fun getLayoutId(): Int {
        return R.layout.fragment_main
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pagerAdapter = MainViewPagerAdapter(requireFragmentManager(), requireContext())
        viewPager.adapter = pagerAdapter
        tabsLayout.setupWithViewPager(viewPager)
    }
}