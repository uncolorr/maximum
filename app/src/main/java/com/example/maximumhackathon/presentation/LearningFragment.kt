package com.example.maximumhackathon.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.maximumhackathon.R
import com.example.maximumhackathon.domain.engines.FBEngine
import com.example.maximumhackathon.presentation.base.BaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_page_learning.*

class LearningFragment: BaseFragment() {

    private lateinit var lessonsAdapter: LessonsAdapter

    private val fbEngine = FBEngine()

    private val compositeDisposable = CompositeDisposable()

    override fun getLayoutId(): Int {
        return R.layout.fragment_page_learning
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fbEngine.wordsObserver
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                // TODO need some holder start
                fbEngine.getPartOfWords(50, 10L)
            }
            .doFinally {
                // TODO need some holder stop
            }
            .subscribe {
                Log.i("Logcat ", "wordsList $it")
            }
            .disposeOnDestroy()

        fbEngine.lessonsObserver
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                // TODO need some holder start
                fbEngine.getLessonsList()
            }
            .doFinally {
                // TODO need some holder stop
            }
            .subscribe {
                Log.i("Logcat ", "lessonsList $it")
                lessonsAdapter.setItems(it)
            }
            .disposeOnDestroy()

        lessonsAdapter = LessonsAdapter()
        recyclerViewLessons.apply {
            adapter = lessonsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun Disposable.disposeOnDestroy() {
        compositeDisposable.add(this)
    }
}