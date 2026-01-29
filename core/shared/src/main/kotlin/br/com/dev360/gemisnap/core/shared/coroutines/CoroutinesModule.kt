package br.com.dev360.gemisnap.core.shared.coroutines

import org.koin.dsl.module

val CoroutinesModule = module {
    factory<CustomDispatchers> { DispatchersImpl() }
}