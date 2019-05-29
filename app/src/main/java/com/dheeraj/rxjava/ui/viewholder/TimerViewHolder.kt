package com.dheeraj.rxjava.ui.viewholder

import android.content.Context
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.dheeraj.rxjava.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.item_timer.view.*
import java.util.concurrent.TimeUnit

class TimerViewHolder(private val item: View, private val context: Context): RecyclerView.ViewHolder(item) {

    private val compositeDisposable = CompositeDisposable()

    fun bind(value: Int) {
        var timer = value
        val disposable =
            Observable.interval(0, 1, TimeUnit.SECONDS)
                .flatMap {
                    return@flatMap Observable.create<String> { emitter ->
                        Log.d("IntervalExample", "Create")
                        timer--
                        emitter.onNext("$timer")
                        emitter.onComplete()
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    item.tv_timer.text = context.getString(R.string.timer_value,it)
                }
        compositeDisposable.add(disposable)
    }

}