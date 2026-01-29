package br.com.dev360.gemisnap.core.networking.util

import br.com.dev360.gemisnap.core.networking.typeadapter.UtcDateTypeAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.util.Date

object GsonProvider {
    fun get(): Gson = GsonBuilder()
        .registerTypeAdapter(Date::class.java, UtcDateTypeAdapter())
        .create()
}