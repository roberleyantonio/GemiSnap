package br.com.dev360.gemisnap.core.networking.di.qualifiers

import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.QualifierValue

object QualifierGeminiAiHost: Qualifier {
    override val value: QualifierValue
        get() = "QualifierGeminiAiHost"
}