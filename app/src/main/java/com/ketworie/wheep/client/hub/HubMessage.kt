package com.ketworie.wheep.client.hub

import java.time.ZonedDateTime

data class HubMessage(
    var userName: String,
    var userImage: String,
    var text: String,
    var date: ZonedDateTime
) {
}