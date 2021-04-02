package com.example.maximumhackathon.presentation

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.maximumhackathon.R
import com.example.maximumhackathon.domain.model.Test
import com.example.maximumhackathon.domain.model.Word
import com.example.maximumhackathon.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_test.*

class TestFragment: BaseFragment() {

    private lateinit var variantsAdapter: VariantsAdapter

    override fun getLayoutId(): Int {
        return R.layout.fragment_test
    }

    private fun initToolbar(){
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.title = "5/20"
        toolbar.setNavigationOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()

        this.variantsAdapter = VariantsAdapter()
        recyclerViewVariants.apply {
            adapter = variantsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        val items = arrayListOf(
            Word(
                0,
                "Variant 1",
                frequency = 1
            ),
            Word(
                0,
                "Variant 2",
                frequency = 1
            ),
            Word(
                0,
                "Variant 3",
                frequency = 1
            ),
            Word(
                0,
                "Variant 4",
                frequency = 1
            ),
        )
        variantsAdapter.setItems(items)

        buttonAnswer.setOnClickListener {

        }
    }

    companion object {
        fun newInstance(test: Test): TestFragment {
            val args = Bundle()
            args.putSerializable(ScreenExtraConstants.test, test)
            val fragment = TestFragment()
            fragment.arguments = args
            return fragment
        }
    }
}