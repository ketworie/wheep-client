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
        when (this@toastError) {
            is NetworkResponse.ApiError -> toast(context, this@toastError.body.message)
            is NetworkResponse.NetworkError -> toast(
                context,
                context.resources.getString(R.string.network_error)
            )
            is NetworkResponse.UnknownError -> toast(
                context,
                context.resources.getString(R.string.unknown_error)
            )
            else -> return@withContext false
        }
        return@withContext true
    }

private suspend fun toast(context: Context, text: String) = withContext(Dispatchers.Main) {
    Toast.makeText(context, text.capitalize(), Toast.LENGTH_SHORT).show()
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
