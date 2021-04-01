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
        fbReference.collection("lessons")
            .get()
            .addOnSuccessListener { gotLessonsList ->
                if (gotLessonsList.documents.isNullOrEmpty()) {
                    fbReference.collection("words")
                        .get()
                        .addOnSuccessListener {
                            for (i in 0 until (it.documents.size / LIMIT.toInt())) {

                                val description = emojyList[Random().nextInt(emojyList.size)]

                                lessonsList.add(
                                    Lesson(
                                        id = i,
                                        name = "Урок ${i + 1}",
                                        number = i + 1,
                                        status = LessonStatus.PENDING,
                                        description = description
                                    )
                                )

                                val hm = hashMapOf<String, Any>()
                                hm["id"] = i
                                hm["name"] = "Урок ${i + 1}"
                                hm["number"] = i + 1
                                hm["status"] = LessonStatus.PENDING.code
                                hm["description"] = description

                                fbReference
                                    .collection("lessons")
                                    .add(hm)
                            }

                            lessonsObserver.onNext(lessonsList)
                        }
                } else {
                    fbReference.collection("lessons")
                        .orderBy("number")
                        .addSnapshotListener { value, _ ->
                            value?.documents?.forEach {
                                lessonsList.add(
                                    Lesson(
                                        id = it.data?.get("id").toString().toInt(),
                                        name = it.data?.get("name").toString(),
                                        number = it.data?.get("number").toString().toInt(),
                                        status = LessonStatus.valueByCode(it.data?.get("status").toString()),
                                        description = it.data?.get("description").toString()
                                    )
                                )
                            }
                            lessonsObserver.onNext(lessonsList)
                        }
                }
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
