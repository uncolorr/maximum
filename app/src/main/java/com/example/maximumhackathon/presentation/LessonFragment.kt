package com.example.maximumhackathon.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.maximumhackathon.R
import com.example.maximumhackathon.domain.engines.FBEngine
import com.example.maximumhackathon.domain.model.Word
import com.example.maximumhackathon.presentation.base.BaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_lesson.*

open class LessonFragment : BaseFragment() {

    private val fbEngine = FBEngine()

    private val compositeDisposable = CompositeDisposable()

    var onLessonCompleteListener: (() -> Unit)? = null

    private var words = listOf<Word>()
    private var currentIndex = -1

    override fun getLayoutId(): Int {
        return R.layout.fragment_lesson
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initWords()
        buttonNext.setOnClickListener {
            updateWord()
        }
    }

    private fun initToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    private fun initWords() {
        fbEngine.wordsObserver
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                arguments?.getInt(ScreenExtraConstants.number)?.let { number ->
                    fbEngine.getPartOfWords(number * 20, 20)
                }
            }
            .doFinally {

            }
            .subscribe {
                words = it
                updateWord()
                Log.i("Logcat ", "wordsList $it")
            }
            .disposeOnDestroy()
    }

    private fun updateWord() {
        if (words.isEmpty()) {
            return
        }
        if (currentIndex + 1 == words.size) {
            onLessonCompleteListener?.invoke()
            exitLesson()
            return
        }
        currentIndex++
        toolbar.title = "${currentIndex + 1}/${words.size}"
        textViewWord.text = words[currentIndex].name
        textViewTranslate.text = words[currentIndex].translate
    }

    private fun exitLesson() {
        activity?.supportFragmentManager?.popBackStack()
    }

    private fun Disposable.disposeOnDestroy() {
        compositeDisposable.add(this)
    }

    companion object {
        fun newInstance(number: Int): LessonFragment {
            val args = Bundle()
            args.putInt(ScreenExtraConstants.number, number)
            val fragment = LessonFragment()
            fragment.arguments = args
            return fragment
        }
    }
}