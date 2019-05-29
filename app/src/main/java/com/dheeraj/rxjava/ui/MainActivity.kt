package com.dheeraj.rxjava.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dheeraj.rxjava.R
import com.dheeraj.rxjava.model.User
import com.dheeraj.rxjava.ui.adapter.TimerAdapter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName
    private var users: Observable<User> = getUsers().subscribeOn(Schedulers.io())
    private val compositeDisposable = CompositeDisposable()
    private val timerAdapter = TimerAdapter(
        arrayListOf(
            100, 99, 98, 97, 96
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
         Call one function at a time to closely observe the difference between the map operators.
         */
        // map()
        /*
        flatMap()
        concatMap()
        switchMap()
        */
        setUpRecyclerView()
    }

    /**
     * Maps the user object to string and just returns the name(String) of the user
     */
    private fun map() {
        val disposable = users.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                it.name
            }
            .subscribe({
                Log.d(TAG, it)
            }, {
                it.message?.let {
                    Log.d(TAG, "Error-$it")
                }
            })
        compositeDisposable.add(disposable)
    }

    /**
     * Flattens the result of One observable(getUsers()) with another(addIdToTheUser())
     * and returns an Observable (Order not maintained)
     */
    private fun flatMap() {
        val disposable = users.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap { user ->
                addIdToTheUser(user).subscribeOn(Schedulers.io())
            }.subscribe({
                Log.d(TAG, "${it.name} - ${it.age}  - ${it.id}")
            }, {
                it.message?.let {
                    Log.d(TAG, "Error-$it")
                }
            })
        compositeDisposable.add(disposable)
    }

    /**
     * Concats the result of One observable(getUsers()) with another(addIdToTheUser())
     * and returns an Observable (Order maintained)
     */
    private fun concatMap() {
        val disposable = users.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .concatMap { user ->
                addIdToTheUser(user).subscribeOn(Schedulers.io())
            }.subscribe({
                Log.d(TAG, "${it.name} - ${it.age}  - ${it.id}")
            }, {
                it.message?.let {
                    Log.d(TAG, "Error-$it")
                }
            })
        compositeDisposable.add(disposable)
    }

    /**
     * Switches the result to the latest Observable that is emitted and returns the respective Observable
     */
    private fun switchMap() {
        val disposable = users.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .switchMap { user ->
                addIdToTheUser(user).subscribeOn(Schedulers.io())
            }.subscribe({
                Log.d(TAG, "${it.name} - ${it.age}  - ${it.id}")
            }, {
                it.message?.let {
                    Log.d(TAG, "Error-$it")
                }
            })
        compositeDisposable.add(disposable)
    }

    /**
     * Emits an Observable<User>
     */
    private fun getUsers(): Observable<User> {
        return Observable.create {
            val users = arrayListOf<User>(
                User("Person A", 20),
                User("Person B", 21),
                User("Person C", 22),
                User("Person D", 23)
            )
            users.forEach { user ->
                it.onNext(user)
            }
            it.onComplete()
        }
    }

    /**
     * Adds Id to the provided User
     * @param user the user object for which the Id has to be added
     */
    private fun addIdToTheUser(user: User): Observable<User> {
        val idList = arrayListOf<Int>(1, 2, 3, 4)
        return Observable.create {
            user.id = idList[Random().nextInt(2) + 0]
            // Generate network latency of random duration
            val sleepTime = Random().nextInt(1000) + 500
            Thread.sleep(sleepTime.toLong())
            it.onNext(user)
            it.onComplete()
        }
    }

    /**
     * clear the compositeDisposable stack onDestroy
     */
    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    private fun timer() {
        var count = 0
        val disposable =
            Observable.interval(0, 2, TimeUnit.SECONDS)
                .flatMap {
                    return@flatMap Observable.create<String> { emitter ->
                        Log.d("IntervalExample", "Create")
                        count++
                        emitter.onNext("$count")
                        emitter.onComplete()
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {

                }
        compositeDisposable.add(disposable)
    }

    private fun setUpRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        rv_timers.layoutManager = layoutManager
        rv_timers.adapter = timerAdapter
    }
}
