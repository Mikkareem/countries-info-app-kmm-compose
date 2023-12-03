package viewmodels

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import model.Country
import navigation.componentScope
import network.RestCountriesApi
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import screenevents.CountriesListScreenEvent

class CountriesListComponent(
    componentContext: ComponentContext,
    private val onNavigateToCountryDetailScreen: (String?) -> Unit
): ComponentContext by componentContext, KoinComponent {
    private val _countries = MutableValue(emptyList<Country>())
    val countries: Value<List<Country>> = _countries

    private val _networkError = MutableValue(false)
    val networkError: Value<Boolean> = _networkError

    private val _isLoading = MutableValue(false)
    val isLoading: Value<Boolean> = _isLoading

    private val api by inject<RestCountriesApi>()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        if(throwable::class.simpleName!!.contains("UnknownHostException")) {
            _networkError.update { true }
        } else {
            println("Uncaughtable Exception $throwable")
        }
    }

    init {
        fetchCountries()
    }

    private fun fetchCountries() {
        _isLoading.update { true }
        _networkError.update { false }
        componentScope().launch(coroutineExceptionHandler) {
            delay(5000)
            val tCountries = api.getAllCountries()
            _countries.update { tCountries }
        }.invokeOnCompletion {
            _isLoading.update { false }
        }
    }

    fun onEvent(event: CountriesListScreenEvent) {
        when(event) {
            is CountriesListScreenEvent.ClickOnCountry -> onNavigateToCountryDetailScreen(event.ccn3)
            CountriesListScreenEvent.RetryFetch -> fetchCountries()
        }
    }
}