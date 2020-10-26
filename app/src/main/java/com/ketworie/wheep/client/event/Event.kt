package com.ketworie.wheep.client.event

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.ketworie.wheep.client.chat.Message
import java.time.ZonedDateTime

data class Event(
    val date: ZonedDateTime,
    @JsonTypeInfo(
        include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
        use = JsonTypeInfo.Id.NAME,
        property = "type"
    )
    @JsonSubTypes(JsonSubTypes.Type(value = Message::class, name = "message"))
    val body: EventBody
)
