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
import kotlinx.android.synthetic.main.fragment_test.*
import okhttp3.internal.addHeaderLenient
import java.util.*

class TestFragment : BaseFragment() {

    private lateinit var variantsAdapter: VariantsAdapter

    private val fbEngine = FBEngine()

    private val compositeDisposable = CompositeDisposable()

    lateinit var currentWord: Word

    var buttonStateMark = false

    var items = mutableListOf<Word>()

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
            if (buttonStateMark){

            } else {
                val checkedWord = variantsAdapter.getCheckedWord()
                if (checkedWord != null){
                    if (currentWord.name == checkedWord.name){
//                        Toast.makeText(requireContext(), "Правильно", Toast.LENGTH_SHORT).show()
                        variantsAdapter.setRightItem(variantsAdapter.checkedPosition)
                    } else {
//                        Toast.makeText(requireContext(), "Неправильно", Toast.LENGTH_SHORT).show()
                        variantsAdapter.setWrongItem(variantsAdapter.checkedPosition)

                        var rightPosition = 0
                        items.forEachIndexed { index, word ->
                            if (currentWord.name == word.name){
                                rightPosition = index
                            }
                        }

                        variantsAdapter.setRightItem(rightPosition)
                    }
                    variantsAdapter.notifyDataSetChanged()

                    buttonAnswer.text = "Далее"
                    buttonStateMark = true
                } else {
                    Toast.makeText(requireContext(), "Нучего не выбрано", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val test = arguments?.getSerializable(ScreenExtraConstants.test) as Test
        items = mutableListOf()

        fbEngine.wordsObserver
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
                currentWord = it[Random().nextInt(it.size)]
                textViewWord.text = currentWord.name
                for (i in 0..3) {
                    var subItem: Word
                    do {
                        subItem = it[Random().nextInt(it.size)]
                    } while (subItem.name == currentWord.name)
                    items.add(
                        it[Random().nextInt(it.size)]
                    )
                }
                items[Random().nextInt(4)] = currentWord
                variantsAdapter.setItems(items)
                Log.i("Logcat ", "wordsList of test $it")
            }
            .disposeOnDestroy()
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