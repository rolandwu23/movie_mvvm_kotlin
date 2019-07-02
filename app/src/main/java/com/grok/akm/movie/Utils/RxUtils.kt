package com.grok.akm.movie.Utils

import io.reactivex.disposables.Disposable

class RxUtils {

    fun unsubscribe(subscription:Disposable?) {
        if(subscription != null && !subscription.isDisposed){
            subscription.dispose()
        }
    }

    fun unsubscribe(vararg subscriptions: Disposable?){
        for(subscription in subscriptions){
            unsubscribe(subscription)
        }
    }
}