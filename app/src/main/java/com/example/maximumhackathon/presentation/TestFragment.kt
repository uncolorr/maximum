package com.example.maximumhackathon.presentation

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.maximumhackathon.R
import com.example.maximumhackathon.domain.engines.FBEngine
import com.example.maximumhackathon.domain.model.Test
import com.example.maximumhackathon.domain.model.Word
import com.example.maximumhackathon.presentation.base.BaseFragment
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_test.*

class TestFragment : BaseFragment() {

    private lateinit var variantsAdapter: VariantsAdapter

    private val fbEngine = FBEngine()

    private val compositeDisposable = CompositeDisposable()

    override fun getLayoutId(): Int {
        return R.layout.fragment_test
    }

    private fun initToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.title = "5/20"
        toolbar.setNavigationOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()

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
                fbEngine.getPartOfWords(test.number * 20)
            }
            .doFinally {
                // TODO need some holder stop
            }
            .subscribe {
                Log.i("Logcat ", "wordsList $it")
            }
            .disposeOnDestroy()

        this.variantsAdapter = VariantsAdapter()
        recyclerViewVariants.apply {
            adapter = variantsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        val items = arrayListOf(
            Word(
                0,
                "Variant 1",
                frequency = 1
            ),
            Word(
                0,
                "Variant 2",
                frequency = 1
            ),
            Word(
                0,
                "Variant 3",
                frequency = 1
            ),
            Word(
                0,
                "Variant 4",
                frequency = 1
            ),
        )
        variantsAdapter.setItems(items)

        buttonAnswer.setOnClickListener {

        }
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