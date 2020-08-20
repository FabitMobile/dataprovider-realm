package ru.fabit.dataproviderrealm

import java.io.IOException

class HashMapResultHandler<TKey, TValue> : ResultHandler<HashMap<TKey, TValue>>() {

    override fun handleResult(rawJson: String?): HashMap<TKey, TValue> {
        var hashMap = HashMap<TKey, TValue>()
        val resultHandler = ObjectResultHandler(HashMap::class.java)
        try {
            hashMap = resultHandler.handleResult(rawJson) as HashMap<TKey, TValue>
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return hashMap
    }
}