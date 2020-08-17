package com.ketworie.wheep.client.network

import java.time.ZonedDateTime

data class ApiError(val message: String, val date: ZonedDateTime, val path: String)