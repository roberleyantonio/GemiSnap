package br.com.dev360.gemisnap.core.shared.di

import br.com.dev360.gemisnap.core.shared.util.AndroidBitmapDecoder
import br.com.dev360.gemisnap.core.shared.util.BitmapDecoder
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val CoreSharedModule = module {
    single<BitmapDecoder> { AndroidBitmapDecoder(androidContext().contentResolver) }
}