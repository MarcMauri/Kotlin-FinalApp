package es.marcmauri.finalapp.utils

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

object RxBus {

    private val publisher = PublishSubject.create<Any>()

    fun publish(event: Any) {
        // onNext publica el evento a todas las personas que esten escuchando
        publisher.onNext(event)
    }

    fun <T> listen(eventType: Class<T>): Observable<T> = publisher.ofType(eventType)
}