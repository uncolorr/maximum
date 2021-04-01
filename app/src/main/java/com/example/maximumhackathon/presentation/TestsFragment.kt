package com.example.maximumhackathon.presentation

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.maximumhackathon.R
import com.example.maximumhackathon.domain.model.Lesson
import com.example.maximumhackathon.domain.model.LessonStatus
import com.example.maximumhackathon.domain.model.Test
import com.example.maximumhackathon.domain.model.TestStatus
import com.example.maximumhackathon.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_page_learning.*
import kotlinx.android.synthetic.main.fragment_page_tests.*

class TestsFragment: BaseFragment(){

    private lateinit var testsAdapter: TestsAdapter

    override fun getLayoutId(): Int {
        return R.layout.fragment_page_tests
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        testsAdapter = TestsAdapter()
        recyclerViewTests.apply {
            adapter = testsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        val list = arrayListOf(
            Test(
                0,
                "Тест 1",
                TestStatus.COMPLETED,
                "100%",
                1,
                "😉"
            ),
            Test(
                0,
                "Тест 2",
                TestStatus.PENDING,
                "100%",
                2,
                "❤"
            ),
            Test(
                0,
                "Тест 3",
                TestStatus.BLOCKED,
                " - ",
                3,
                "😊"
            ),
            Test(
                0,
                "Тест 4",
                TestStatus.BLOCKED,
                " - ",
                4,
                "🤷‍♀️"
            ),
            Test(
                0,
                "Тест 5",
                TestStatus.BLOCKED,
                " - ",
                5,
                "💋"
            )
        )
        testsAdapter.setItems(list)
    }
}