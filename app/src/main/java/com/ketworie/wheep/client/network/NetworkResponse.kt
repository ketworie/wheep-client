package com.ketworie.wheep.client.network

import android.content.Context
import android.widget.Toast
import com.ketworie.wheep.client.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

typealias GenericError<T> = NetworkResponse<T, ApiError>

suspend fun <T : Any> GenericError<T>.toastError(context: Context): Boolean =
    withContext(Dispatchers.Default) {
        val errorMessage = errorMessage(context) ?: return@withContext false
        toast(context, errorMessage)
        return@withContext true
    }

private suspend fun toast(context: Context, text: String) = withContext(Dispatchers.Main) {
    Toast.makeText(context, text.capitalize(), Toast.LENGTH_SHORT).show()
}

fun <T : Any> GenericError<T>.errorMessage(context: Context): String? =
    when (this) {
        is NetworkResponse.ApiError -> this.body.message
        is NetworkResponse.NetworkError -> context.resources.getString(R.string.network_error)
        is NetworkResponse.UnknownError -> context.resources.getString(R.string.unknown_error)
        else -> null
    }


sealed class NetworkResponse<out T : Any, out U : Any> {
    /**
     * Success response with body
     */
    data class Success<T : Any>(val body: T) : NetworkResponse<T, Nothing>()

    /**
     * Failure response with body
     */
    data class ApiError<U : Any>(val body: U, val code: Int) : NetworkResponse<Nothing, U>()

    /**
     * Network error
     */
    data class NetworkError(val error: IOException) : NetworkResponse<Nothing, Nothing>()

    /**
     * For example, json parsing error
     */
    data class UnknownError(val error: Throwable) : NetworkResponse<Nothing, Nothing>()
}
