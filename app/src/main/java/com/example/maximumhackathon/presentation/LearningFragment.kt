package com.example.maximumhackathon.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.maximumhackathon.R
import com.example.maximumhackathon.domain.model.Lesson
import com.example.maximumhackathon.domain.model.LessonStatus
import com.example.maximumhackathon.domain.model.Word
import com.example.maximumhackathon.presentation.base.BaseFragment
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_page_learning.*

class LearningFragment: BaseFragment() {

    private lateinit var lessonsAdapter: LessonsAdapter

    private val fbReference = FirebaseFirestore.getInstance()

    override fun getLayoutId(): Int {
        return R.layout.fragment_page_learning
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val wordsList = mutableListOf<Word>()

        fbReference.collection("words").get()
            .addOnSuccessListener { snapShot ->
                snapShot.documents.forEach {
                    wordsList.add(
                        Word(
                            it.data?.get("orderNumber").toString().toInt(),
                            it.data?.get("name").toString(),
                            it.data?.get("orderNumber").toString().toLong()
                        )
                    )
                }
            }

        Log.i("Logcat ", "wordsList $wordsList")

        lessonsAdapter = LessonsAdapter()
        recyclerViewLessons.apply {
            adapter = lessonsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        val list = arrayListOf(
            Lesson(
                0,
                "Урок 1",
                1,
                LessonStatus.STARTED,
                "❤"
            ),
            Lesson(
                0,
                "Урок 2",
                1,
                LessonStatus.STARTED,
                "😊"
            ),
            Lesson(
                0,
                "Урок 3",
                1,
                LessonStatus.STARTED,
                "😉"
            ), Lesson(
                0,
                "Урок 4",
                1,
                LessonStatus.STARTED,
                "💋"
            ), Lesson(
                0,
                "Урок 5",
                1,
                LessonStatus.STARTED,
                "🤷‍♀️"
            )
        )
        lessonsAdapter.setItems(list)
    }
}