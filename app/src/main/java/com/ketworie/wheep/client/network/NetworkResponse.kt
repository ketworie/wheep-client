package com.ketworie.wheep.client.network

import android.app.Activity
import android.widget.Toast
import com.ketworie.wheep.client.R
import java.io.IOException

typealias GenericError<T> = NetworkResponse<T, ApiError>

fun <T : Any> GenericError<T>.toast(activity: Activity) {
    when (this) {
        is NetworkResponse.ApiError -> toast(activity, this.body.message)
        is NetworkResponse.NetworkError -> toast(
            activity,
            activity.resources.getString(R.string.network_error)
        )
        is NetworkResponse.UnknownError -> toast(
            activity,
            activity.resources.getString(R.string.unknown_error)
        )
    }
}

private fun toast(activity: Activity, text: String) {
    activity.runOnUiThread {
        Toast.makeText(activity, text.capitalize(), Toast.LENGTH_SHORT)
            .show()
    }
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
