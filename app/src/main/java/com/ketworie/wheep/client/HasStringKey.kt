package com.ketworie.wheep.client

interface HasStringKey {

    fun getPosition(key: String): Int?

    fun getKey(position: Int): String?
}