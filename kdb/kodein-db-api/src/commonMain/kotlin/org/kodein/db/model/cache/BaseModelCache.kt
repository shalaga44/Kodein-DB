package org.kodein.db.model.cache

import org.kodein.db.Key
import org.kodein.db.Sized

interface BaseModelCache {

    fun <M : Any> getEntry(key: Key<M>): ModelCache.Entry<M>

    operator fun <M : Any> get(key: Key<M>): M? = getEntry(key).model

    fun <M : Any> getOrRetrieveEntry(key: Key<M>, retrieve: () -> Sized<M>?): ModelCache.Entry<M>

    fun <M : Any> getOrRetrieve(key: Key<M>, retrieve: () -> Sized<M>?): M? = getOrRetrieveEntry(key, retrieve).model

    fun <M : Any> put(key: Key<M>, value: M, size: Int)

    fun <M : Any> put(key: Key<M>, sized: Sized<M>) = put(key, sized.model, sized.size)

    fun <M : Any> delete(key: Key<M>): ModelCache.Entry<M>

    fun <M : Any> evict(key: Key<M>): ModelCache.Entry<M>

    fun clear()

    fun batch(block: BaseModelCache.() -> Unit)

}
