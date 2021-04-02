package com.example.maximumhackathon.presentation

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.maximumhackathon.R
import com.example.maximumhackathon.domain.engines.FBEngine
import com.example.maximumhackathon.presentation.base.BaseFragment
import com.example.maximumhackathon.transaction
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
                progressBar.visibility = View.VISIBLE
                fbEngine.getLessonsList()
            }
            .doFinally {

            }
            .subscribe {
                Log.i("Logcat ", "lessonsList $it")
                progressBar.visibility = View.GONE
                lessonsAdapter.setItems(it)
            }
            .disposeOnDestroy()

        lessonsAdapter = LessonsAdapter().apply {
            onItemClickListener = {
                openLessonScreen(it.number)
            }

            onBlockedItemClickListener = {

            }
        }
        recyclerViewLessons.apply {
            adapter = lessonsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun openLessonScreen(number: Int) {
        val fragment = LessonFragment.newInstance(number)
        fragment.onLessonCompleteListener = {
            lessonsAdapter.notifyDataSetChanged()
            showCompleteLessonDialog()
        }
        activity?.supportFragmentManager.transaction {
            add(
                R.id.mainContainer,
                fragment,
                LessonFragment::class.java.name
            )
            addToBackStack(LessonFragment::class.java.name)
        }
    }

    private fun showCompleteLessonDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Поздравляем")
            .setMessage("Урок завершен!")
            .setPositiveButton("ОК") { dialog, id ->
                dialog.cancel()
            }
        val alert = builder.create()
        alert.show()
    }

    protected fun Disposable.disposeOnDestroy() {
        compositeDisposable.add(this)
    }
}