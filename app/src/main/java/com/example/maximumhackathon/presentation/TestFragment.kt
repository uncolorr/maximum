package com.example.maximumhackathon.presentation

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.maximumhackathon.R
import com.example.maximumhackathon.domain.engines.FBEngine
import com.example.maximumhackathon.domain.model.Lesson
import com.example.maximumhackathon.domain.model.LessonStatus
import com.example.maximumhackathon.domain.model.Test
import com.example.maximumhackathon.domain.model.Word
import com.example.maximumhackathon.presentation.base.BaseFragment
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_lesson.*
import kotlinx.android.synthetic.main.fragment_test.*
import kotlinx.android.synthetic.main.fragment_test.progressBar
import kotlinx.android.synthetic.main.fragment_test.textViewWord
import kotlinx.android.synthetic.main.fragment_test.toolbar
import java.util.*

class TestFragment : BaseFragment() {

    private lateinit var variantsAdapter: VariantsAdapter

    private val fbEngine = FBEngine()

    private val compositeDisposable = CompositeDisposable()

    private var testWords: List<Word> = arrayListOf()

    private var currectWordIndex: Int = -1

    private var subWords: List<Word> = arrayListOf()

    private var scores: Int = 0

    private var isShowedAnswer = false


    override fun getLayoutId(): Int {
        return R.layout.fragment_test
    }

    private fun initToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()

        variantsAdapter = VariantsAdapter()
        recyclerViewVariants.apply {
            adapter = variantsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        buttonAnswer.setOnClickListener {
            variantsAdapter.getCheckedWord()?.let { checkedWord ->
                if (testWords.isEmpty()) {
                    return@setOnClickListener
                }
                if (!isShowedAnswer) {
                    val currentWord = testWords[currectWordIndex]
                    if (checkedWord.translate == currentWord.translate) {
                        scores++
                    }
                    isShowedAnswer = true
                    variantsAdapter.showAnswer(currentWord)
                    buttonAnswer.text = requireContext().getString(R.string.next)
                } else {
                    buttonAnswer.text = requireContext().getString(R.string.answer)
                    updateTest()
                    isShowedAnswer = false
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val test = arguments?.getSerializable(ScreenExtraConstants.test) as Test
        Observable.zip(
            fbEngine.wordsObserver,
            fbEngine.subWordsObserver,
            { words, subwords ->
                Pair(words, subwords)
            }
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                // TODO need some holder start
                progressBar.visibility = View.VISIBLE
                recyclerViewVariants.visibility = View.INVISIBLE
                textViewWord.visibility = View.INVISIBLE
                buttonAnswer.visibility = View.INVISIBLE
                fbEngine.getPartOfWords(test.number * 20)
                fbEngine.getSubWordsForTest(test.number)
            }
            .doFinally {
                // TODO need some holder stop
            }
            .doOnError {
                Log.i("Logcat ", it.stackTraceToString())
            }
            .subscribe {
                recyclerViewVariants.visibility = View.VISIBLE
                buttonAnswer.visibility = View.VISIBLE
                textViewWord.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                testWords = it.first.filter { word ->
                    word.translate.isNotEmpty() && word.translate != "***"
                }
                subWords = it.second.filter { word ->
                    word.translate.isNotEmpty() && word.translate != "***"
                }
                updateTest()
            }
            .disposeOnDestroy()
    }

    private fun updateTest() {
        if (testWords.isEmpty()) {
            return
        }
        currectWordIndex++
        if (currectWordIndex + 1 == testWords.size) {
            activity?.supportFragmentManager?.popBackStack()
            return
        }
        val currentWord = testWords[currectWordIndex]
        val variants = arrayListOf<Word>()
        for (i in 0..3) {
            variants.add(subWords[Random().nextInt(subWords.size)])
        }
        variants[Random().nextInt(3)] = currentWord
        textViewWord.text = currentWord.name
        variantsAdapter.setItems(variants)
        variantsAdapter.resetCheck()
        toolbar?.title = "${currectWordIndex + 1}/${testWords.size}"
    }

    private fun Disposable.disposeOnDestroy() {
        compositeDisposable.add(this)
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