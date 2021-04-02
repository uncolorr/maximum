package com.example.maximumhackathon.domain.engines

import android.util.Log
import com.example.maximumhackathon.domain.model.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.*

class FBEngine {

    private val fbReference = FirebaseFirestore.getInstance()

    private val yandexEngine = YandexEngine()
    val wordsObserver = PublishSubject.create<List<Word>>()
    val subWordsObserver = PublishSubject.create<List<Word>>()
    val lessonsObserver = PublishSubject.create<List<Lesson>>()
    val testsObserver = PublishSubject.create<List<Test>>()

    private val emojyList = mutableListOf<String>()

    init {
        for (i in 0x1F601..0x1F64F) {
            emojyList.add(getEmojiByUnicode(i))
        }
    }

    fun getLessonsList() {
        val user = Firebase.auth.currentUser?.providerData?.first()?.email
        val lessonsList = mutableListOf<Lesson>()

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
                                        description = description,
                                        dbReference = "",
                                        user = user
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

                    val ref = fbReference.collection("lessons")
                        .orderBy("number")

                    ref.get()
                        .addOnSuccessListener { value ->
                            value?.documents?.forEach {
                                lessonsList.add(
                                    Lesson(
                                        id = it.data?.get("id").toString().toInt(),
                                        name = it.data?.get("name").toString(),
                                        number = it.data?.get("number").toString().toInt(),
                                        status = LessonStatus.valueByCode(
                                            it.data?.get("status").toString()
                                        ),
                                        description = it.data?.get("description").toString(),
                                        dbReference = it.reference.id,
                                        user = user
                                    )
                                )
                            }
                            lessonsObserver.onNext(lessonsList)

                        }
                }
            }
    }

    fun getPartOfWords(offset: Int, limit: Long = LIMIT) {

        Log.i("Logcat ", "getPartOfWords")

        val wordsList = mutableListOf<Word>()

        var restCounter = limit.toInt() - 1

        val ref = fbReference.collection("words")
            .orderBy("orderNumber")
            .limit(limit)
            .startAt(offset)

        ref.get()
            .addOnSuccessListener { value ->
                value?.documents?.forEach { fbDocument ->
                    if (fbDocument.data?.get("translate") == "***") {
                        yandexEngine.translate(fbDocument.data?.get("name").toString())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { tr ->
                                Log.i(
                                    "Logcat ",
                                    "translate ${tr.def.firstOrNull()?.tr?.firstOrNull()?.text ?: ""}"
                                )
                                val translatedWord = Word(
                                    orderNumber = fbDocument.data?.get("orderNumber").toString()
                                        .toInt(),
                                    name = fbDocument.data?.get("name").toString(),
                                    translate = tr.def.firstOrNull()?.tr?.firstOrNull()?.text ?: "",
                                    frequency = fbDocument.data?.get("frequency").toString()
                                        .toLong()
                                )
                                wordsList.add(translatedWord)

                                fbReference
                                    .collection("words")
                                    .document(fbDocument.reference.id)
                                    .update(
                                        "translate",
                                        tr.def.firstOrNull()?.tr?.firstOrNull()?.text ?: ""
                                    )

                                if (restCounter == 0) {
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

                        if (restCounter == 0) {
                            wordsObserver.onNext(wordsList)
                        } else {
                            restCounter--
                        }
                    }
                }
            }
    }

    fun getTestsList() {

        val testsList = mutableListOf<Test>()

        val user = Firebase.auth.currentUser?.providerData?.first()?.email

        fbReference.collection("tests")
            .whereEqualTo("user", user)
            .get()
            .addOnSuccessListener { gotTestsList ->
                if (gotTestsList.documents.isNullOrEmpty()) {
                    fbReference.collection("words")
                        .get()
                        .addOnSuccessListener {
                            for (i in 0 until (it.documents.size / LIMIT.toInt())) {

                                val description = emojyList[Random().nextInt(emojyList.size)]

                                testsList.add(
                                    Test(
                                        id = i,
                                        name = "Тест ${i + 1}",
                                        status = TestStatus.BLOCKED,
                                        stats = " - ",
                                        number = i + 1,
                                        description = description,
                                        dbReference = "",
                                        user = user
                                    )
                                )

                                val hm = hashMapOf<String, Any>()
                                hm["id"] = i
                                hm["name"] = "Тест ${i + 1}"
                                hm["status"] = LessonStatus.PENDING.code
                                hm["stats"] = " - "
                                hm["number"] = i + 1
                                hm["description"] = description
                                hm["user"] = user ?: ""

                                fbReference
                                    .collection("tests")
                                    .add(hm)
                            }

                            testsObserver.onNext(testsList)
                        }
                } else {

                    fbReference.collection("tests")
                        .orderBy("number")
                        .whereEqualTo("user", user)
                        .get()
                        .addOnSuccessListener { testsValue ->

                            fbReference.collection("lessons")
                                .orderBy("number")
                                .whereEqualTo("status", LessonStatus.COMPLETED.code)
                                .get()
                                .addOnSuccessListener { lessonsValue ->

                                    Log.i("Logcat ", "testsList testsValue $testsValue")
                                    testsValue?.documents?.forEach {

                                        val lastCompleteNumber = lessonsValue.documents[lessonsValue.documents.lastIndex].data?.get("number").toString().toInt()

                                        var currentStatus = TestStatus.valueByCode(it.data?.get("status").toString())

                                        if (it.data?.get("number").toString().toInt() > lastCompleteNumber){

                                            currentStatus = TestStatus.BLOCKED
                                        } else {
                                            if (currentStatus != TestStatus.COMPLETED){
                                                currentStatus = TestStatus.PENDING
                                            }
                                        }

                                        testsList.add(
                                            Test(
                                                id = it.data?.get("id").toString().toInt(),
                                                name = it.data?.get("name").toString(),
                                                status = currentStatus,
                                                stats = it.data?.get("stats").toString(),
                                                number = it.data?.get("number").toString().toInt(),
                                                description = it.data?.get("description")
                                                    .toString(),
                                                dbReference = it.reference.id,
                                                user = it.data?.get("user").toString()
                                            )
                                        )
                                    }
                                    testsObserver.onNext(testsList)

                                }
                                .addOnFailureListener {
                                    Log.i("Logcat ", "testsList error $it")
                                }
                        }
                }
            }
    }

    fun updateLessonStatus(dbReference: String, status: LessonStatus) {
        fbReference
            .collection("lessons")
            .document(dbReference)
            .update("status", status.code)
    }

    fun updateTestStatus(dbReference: String, status: TestStatus) {
        fbReference
            .collection("tests")
            .document(dbReference)
            .update("status", status.code)
    }

    fun updateTestStates(dbReference: String, stats: String) {
        fbReference
            .collection("tests")
            .document(dbReference)
            .update("stats", stats)
    }


    fun getSubWordsForTest(testNumber: Int) {

        val subWordsList = mutableListOf<Word>()

        val offset = ((testNumber / 200) - 1) * 200

        var restCounter = 199

        val ref = fbReference.collection("words")
            .orderBy("orderNumber")
            .limit(200)
            .startAt(offset)

        ref.get()
            .addOnSuccessListener { value ->
                value?.documents?.forEach { fbDocument ->
                    val word = Word(
                        orderNumber = fbDocument.data?.get("orderNumber").toString().toInt(),
                        name = fbDocument.data?.get("name").toString(),
                        translate = fbDocument.data?.get("translate").toString(),
                        frequency = fbDocument.data?.get("frequency").toString().toLong()
                    )
                    subWordsList.add(word)

                    if (restCounter == 0) {
                        subWordsObserver.onNext(subWordsList)
                    } else {
                        restCounter--
                    }
                }

            }
            .addOnFailureListener {
                Log.i("Logcat ", "getSubWordsForTest error $it")
            }
    }

    private fun getEmojiByUnicode(unicode: Int): String {
        return String(Character.toChars(unicode))
    }

    companion object {
        const val OFFSET = 20
        const val LIMIT = 20L
    }
}
