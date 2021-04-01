package com.example.maximumhackathon.domain.engines

import com.example.maximumhackathon.domain.model.Lesson
import com.example.maximumhackathon.domain.model.LessonStatus
import com.example.maximumhackathon.domain.model.Word
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.subjects.PublishSubject
import java.util.*

class FBEngine {

    private val fbReference = FirebaseFirestore.getInstance()

    private val wordsList = mutableListOf<Word>()
    private val lessonsList = mutableListOf<Lesson>()

    val wordsObserver = PublishSubject.create<List<Word>>()
    val lessonsObserver = PublishSubject.create<List<Lesson>>()

    private val emojyList = listOf("❤", "😊", "😉", "💋", "🤷‍", "♀")

    fun getLessonsList() {
        fbReference.collection("words")
            .get()
            .addOnSuccessListener {
                for (i in 0 until (it.documents.size / LIMIT.toInt())) {
                    lessonsList.add(
                        Lesson(
                            id = i,
                            name = "Урок ${i + 1}",
                            number = i + 1,
                            status = LessonStatus.NOT_STARTED,
                            description = emojyList[Random().nextInt(emojyList.size)]
                        )
                    )
                }

                lessonsObserver.onNext(lessonsList)
            }
    }

    fun getPartOfWords(offset: Int, limit: Long) {

        fbReference.collection("words")
            .orderBy("orderNumber")
            .startAt(OFFSET)
            .limit(LIMIT)
            .addSnapshotListener { value, _ ->
                value?.documents?.forEach {
                    wordsList.add(
                        Word(
                            orderNumber = it.data?.get("orderNumber").toString().toInt(),
                            name = it.data?.get("name").toString(),
                            frequency = it.data?.get("frequency").toString().toLong()
                        )
                    )
                }

                // TODO Here will be yandex translate integrations

                wordsObserver.onNext(wordsList)
            }
    }

    companion object {
        const val OFFSET = 20
        const val LIMIT = 20L
    }
}
