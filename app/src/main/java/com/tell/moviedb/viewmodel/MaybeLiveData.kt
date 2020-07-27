package com.tell.moviedb.viewmodel

import androidx.lifecycle.LiveData
import com.tell.moviedb.network.RetrofitResult
import io.reactivex.Maybe
import io.reactivex.disposables.Disposable

class MaybeLiveData<T>(private val maybe: Maybe<T>) : LiveData<RetrofitResult<T>>() {

    private var disposable: Disposable? = null

    override fun onActive() {
        super.onActive()
        disposable = maybe.subscribe({
            value = RetrofitResult.Success(it)
        },{
            value = RetrofitResult.Error(it)
        })
    }

    override fun onInactive() {
        super.onInactive()
        disposable?.dispose()
    }
}