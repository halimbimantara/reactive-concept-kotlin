package com.baculsoft.sample.kotlinreactive.maybe

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import com.baculsoft.sample.kotlinreactive.R
import com.baculsoft.sample.kotlinreactive.ext.statusBarHeight
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.find
import org.jetbrains.anko.setContentView

class MaybeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MaybeUI().setContentView(this)
        setToolbar()
        addListener()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setToolbar() {
        val toolbar = find<Toolbar>(R.id.toolbar_maybe)
        toolbar.title = resources.getString(R.string.menu_maybe)
        toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.bg_arrow_back)
        toolbar.setPadding(0, toolbar.statusBarHeight, 0, 0)

        setSupportActionBar(toolbar)
    }

    private fun addListener() {
        val button = find<Button>(R.id.btn_maybe)
        val textView = find<TextView>(R.id.tv_maybe)
        button.setOnClickListener { doSubscribe(textView) }
    }

    private fun doSubscribe(textView: TextView) {
        val questions = arrayListOf("Yes", "or", "No")
        Maybe.create<List<String>> { maybeEmitter ->
            try {
                when { questions.isNotEmpty() -> maybeEmitter.onSuccess(questions) else -> maybeEmitter.onComplete() }
            } catch (e: Exception) {
                maybeEmitter.onError(e)
            }
        }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({ textView.text = it.size.toString() }, { it.printStackTrace() })
    }
}