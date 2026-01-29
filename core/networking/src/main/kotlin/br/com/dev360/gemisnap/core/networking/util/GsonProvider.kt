package br.com.dev360.gemisnap.core.networking.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder

object GsonProvider {
    fun get(): Gson = GsonBuilder()
        .create()
}