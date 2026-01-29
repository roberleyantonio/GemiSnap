package br.com.dev360.gemisnap.feature.snap.hub.di

import br.com.dev360.gemisnap.core.extensions.provideRetrofitApi
import br.com.dev360.gemisnap.feature.snap.hub.data.HubSnapRemoteDataSourceImpl
import br.com.dev360.gemisnap.feature.snap.hub.data.HubSnapRepositoryImpl
import br.com.dev360.gemisnap.feature.snap.hub.domain.HubSnapContract
import br.com.dev360.gemisnap.feature.snap.hub.presentation.HubSnapUiModelImpl
import br.com.dev360.gemisnap.feature.snap.hub.presentation.HubSnapViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val snapModule = module {
    viewModelOf(::HubSnapViewModel)

    factoryOf(::HubSnapRepositoryImpl) bind HubSnapContract.Repository::class

    factory<HubSnapContract.UiModel> {
        HubSnapUiModelImpl(resources = androidContext().resources, decoder = get())
    }

    factory<HubSnapContract.RemoteDataSource> {
        HubSnapRemoteDataSourceImpl(
            service = provideRetrofitApi()
        )
    }
}