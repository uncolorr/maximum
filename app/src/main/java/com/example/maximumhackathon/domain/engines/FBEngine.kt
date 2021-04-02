package com.example.maximumhackathon.domain.engines

import android.util.Log
import com.example.maximumhackathon.domain.model.Lesson
import com.example.maximumhackathon.domain.model.LessonStatus
import com.example.maximumhackathon.domain.model.Word
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.*

class FBEngine {

    private val fbReference = FirebaseFirestore.getInstance()

    private val yandexEngine = YandexEngine()

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

        var restCounter = limit.toInt() - 1

        fbReference.collection("words")
            .orderBy("orderNumber")
            .startAt(offset)
            .limit(limit)
            .addSnapshotListener { value, _ ->
                value?.documents?.forEach { fbDocument ->
                    if (fbDocument.data?.get("translate") == null){
                        yandexEngine.translate(fbDocument.data?.get("name").toString())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { tr ->
                                Log.i("Logcat ", "translate ${tr.def.firstOrNull()?.tr?.firstOrNull()?.text ?: ""}")
                                val translatedWord = Word(
                                    orderNumber = fbDocument.data?.get("orderNumber").toString().toInt(),
                                    name = fbDocument.data?.get("name").toString(),
                                    translate = tr.def.firstOrNull()?.tr?.firstOrNull()?.text ?: "",
                                    frequency = fbDocument.data?.get("frequency").toString().toLong()
                                )
                                wordsList.add(translatedWord)

                                fbReference
                                    .collection("words")
                                    .document(fbDocument.reference.id)
                                    .update("translate", tr.def.firstOrNull()?.tr?.firstOrNull()?.text ?: "")

                                if (restCounter == 0){
                                    wordsObserver.onNext(wordsList)
                                } else {
                                    restCounter--
                                }
                            }
                    } else {
                        val translatedWord = Word(
                            orderNumber = fbDocument.data?.get("orderNumber").toString().toInt(),
                            name = fbDocument.data?.get("name").toString(),
                            translate = fbDocument.data?.get("translate").toString(),
                            frequency = fbDocument.data?.get("frequency").toString().toLong()
                        )
                        wordsList.add(translatedWord)

                        if (restCounter == 0){
                            wordsObserver.onNext(wordsList)
                        } else {
                            restCounter--
                        }
                    }
                }
            }
    }

    companion object {
        const val OFFSET = 20
        const val LIMIT = 20L
    }
}
