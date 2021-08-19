package com.maikkkko1.android_base_arq.arq.parse_platform

import com.parse.*
import com.parse.ktx.putOrIgnore
import com.parse.livequery.ParseLiveQueryClient
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.reflect.KClass

/**
 * Created by Maikon Ferreira on 7/29/2021.
 * me@maikonferreira.tech
 */

/**
 * Call a parse function.
 *
 * @param T return type
 * @param fnName function name
 * @param params function params
 * @return
 */
suspend inline fun <reified T : Any> callParseFunction(
    fnName: String, params: HashMap<String, *> = hashMapOf<String, Any>()
): T {
    return suspendCoroutine { continuation ->
        ParseCloud.callFunctionInBackground(fnName, params, FunctionCallback<Any> { result, e ->
            if (result != null) continuation.resume(result as T)
            else continuation.resumeWithException(e.build())
        })
    }
}

/**
 * Call a parse function and map (cast) to a custom object.
 *
 * @param T return type
 * @param fnName function name
 * @param params function params
 * @param mapToObject object to be mapped. Ex: Cat::class
 * @return
 */
suspend inline fun <reified T : Any> callParseFunction(
    fnName: String,
    params: HashMap<String, *> = hashMapOf<String, Any>(),
    mapToObject: KClass<T>,
): T {
    return suspendCoroutine { continuation ->
        ParseCloud.callFunctionInBackground(fnName, params, FunctionCallback<Any> { result, e ->
            if (result != null) continuation.resume(mapToObject((result as HashMap<Any, Any?>), mapToObject))
            else continuation.resumeWithException(e.build())
        })
    }
}

/**
 * Call a parse function and map (cast) to a list of a custom object.
 *
 * @param T return type
 * @param fnName function name
 * @param params function params
 * @param mapToListOfObject object to be mapped. Ex: Cat::class
 * @return
 */
suspend inline fun <reified T : Any> callParseFunction(
    fnName: String,
    mapToListOfObject: KClass<T>,
    params: HashMap<String, *> = hashMapOf<String, Any>(),
): MutableList<T> {
    return suspendCoroutine { continuation ->
        ParseCloud.callFunctionInBackground(fnName, params, FunctionCallback<Any> { result, e ->
            if (result != null) continuation.resume(mapToListOfObject((result as List<HashMap<Any, Any?>>), mapToListOfObject))
            else continuation.resumeWithException(e.build())
        })
    }
}

suspend inline fun <reified T : ParseObject> ParseQuery<T>.awaitCount(): Int {
    return suspendCoroutine { continuation ->
        countInBackground { count, e ->
            continuation.resolveParseCall(count, e)
        }
    }
}

suspend inline fun <reified T : ParseObject> ParseQuery<T>.awaitFindAll(): List<T> {
    return suspendCoroutine { continuation ->
        findInBackground { result, e ->
            continuation.resolveParseCall(result, e)
        }
    }
}

suspend inline fun <reified T : ParseObject> ParseQuery<T>.awaitFindFirst(): T {
    return suspendCoroutine { continuation ->
        getFirstInBackground { result, e ->
            continuation.resolveParseCall(result, e)
        }
    }
}

inline fun <reified T : ParseObject> getQuery(): ParseQuery<T> = ParseQuery.getQuery(T::class.java)

inline fun <reified T : ParseObject> ParseQuery<T>.subscribe(crossinline onNewMessageCallback: ((T) -> Unit)) {
    ParseLiveQueryClient.Factory.getClient().subscribe(this).run {
        handleEvents { _, _, obj ->
            onNewMessageCallback.invoke(obj)
        }
    }
}

suspend fun awaitSaveAll(objects: List<ParseObject>): Boolean {
    return suspendCoroutine { continuation ->
        ParseObject.saveAllInBackground(objects) { e ->
            continuation.resolveParseCall(true, e)
        }
    }
}

suspend fun ParseObject.awaitSave(): Boolean {
    return suspendCoroutine { continuation ->
        saveInBackground { e ->
            continuation.resolveParseCall(true, e)
        }
    }
}

suspend fun ParseObject.awaitDelete(): Boolean {
    return suspendCoroutine { continuation ->
        deleteInBackground { e ->
            continuation.resolveParseCall(true, e)
        }
    }
}

suspend fun ParseFile.awaitSave(saveProgressCallback: ((progress: Int) -> Unit)? = null): ParseFile {
    return suspendCoroutine { continuation ->
        saveInBackground({ ex ->
            continuation.resolveParseCall(this, ex)
        }, { uploadProgress ->
            if (uploadProgress > 0) saveProgressCallback?.invoke(uploadProgress)
        })
    }
}

inline fun <reified T : Any> Continuation<T>.resolveParseCall(result: T, ex: ParseException?) {
    if (ex == null) resume(result)
    else resumeWithException(ex.build())
}

fun createOrUpdateParseInstallation(user: Any?) {
    ParseInstallation.getCurrentInstallation().apply { putOrIgnore("user", user) }.saveInBackground()
}

fun ParseQuery<*>.unsubscribe() = ParseLiveQueryClient.Factory.getClient().unsubscribe(this)

fun ParseException.build() = Exception(message ?: "Error!")

fun <T : Any> mapToObject(map: HashMap<Any, Any?>, clazz: KClass<T>): T {
    val constructor = clazz.constructors.first()
    val args = constructor.parameters.map { it to map.get(it.name) }.toMap()

    return constructor.callBy(args)
}

fun <T : Any> mapToListOfObject(list: List<HashMap<Any, Any?>>, clazz: KClass<T>): MutableList<T> {
    val resultList = mutableListOf<T>()

    list.forEach { map ->
        val constructor = clazz.constructors.first()
        val args = constructor.parameters.map { it to map.get(it.name) }.toMap()

        resultList.add(constructor.callBy(args))
    }

    return resultList
}
