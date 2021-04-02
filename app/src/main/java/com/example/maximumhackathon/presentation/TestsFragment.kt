package com.example.maximumhackathon.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.maximumhackathon.R
import com.example.maximumhackathon.domain.engines.FBEngine
import com.example.maximumhackathon.domain.model.Test
import com.example.maximumhackathon.presentation.base.BaseFragment
import com.example.maximumhackathon.transaction
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_page_tests.*

class TestsFragment: BaseFragment(){

    private lateinit var testsAdapter: TestsAdapter

    private val fbEngine = FBEngine()

    private val compositeDisposable = CompositeDisposable()

    override fun getLayoutId(): Int {
        return R.layout.fragment_page_tests
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        testsAdapter = TestsAdapter().apply {
            onItemClickListener = {
                openTestScreen(it)
            }
            onBlockedItemClickListener = {
                //show block dialog
            }
        }
        recyclerViewTests.apply {
            adapter = testsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onResume() {
        super.onResume()
        fbEngine.testsObserver
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                // TODO need some holder start
                fbEngine.getTestsList()
            }
            .doFinally {
                // TODO need some holder stop
            }
            .subscribe {
                Log.i("Logcat ", "testsList $it")
                testsAdapter.setItems(it)
            }
            .disposeOnDestroy()
    }

    private fun openTestScreen(test: Test) {
        activity?.supportFragmentManager.transaction {
            add(
                R.id.mainContainer,
                TestFragment.newInstance(test),
                TestFragment::class.java.name
            )
            addToBackStack(TestFragment::class.java.name)
        }
    }

    private fun Disposable.disposeOnDestroy() {
        compositeDisposable.add(this)
    }
}