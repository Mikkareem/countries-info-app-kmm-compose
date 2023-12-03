package di

import network.RestCountriesApi
import network.RestCountriesApiImpl
import network.provideClient
import org.koin.dsl.module

val appModule = module {
    single { provideClient() }

    single<RestCountriesApi> { RestCountriesApiImpl(get()) }
}