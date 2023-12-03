package viewmodels

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import model.Country
import navigation.componentScope
import network.RestCountriesApi
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import screenevents.CountryDetailScreenEvent

class CountryDetailComponent(
    val ccn3: String?,
    componentContext: ComponentContext,
    private val onNavigateBack: () -> Unit
): ComponentContext by componentContext, KoinComponent {
    private val _country = MutableValue(Country())
    val country: Value<Country> = _country

    private val _networkError = MutableValue(false)
    val networkError: Value<Boolean> = _networkError

    private val _isLoading = MutableValue(true)
    val isLoading: Value<Boolean> = _isLoading

    private val _noDetailsFoundError = MutableValue(false)
    val noDetailsFoundError: Value<Boolean> = _noDetailsFoundError

    private val api by inject<RestCountriesApi>()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        if(throwable::class.simpleName!!.contains("UnknownHostException")) {
            _networkError.update { true }
        } else {
            println("Uncaughtable Exception $throwable")
        }
    }

    init {
        fetchCountryDetails()
    }

    private fun fetchCountryDetails() {
        _isLoading.update { true }
        _networkError.update { false }
        _noDetailsFoundError.update { false }
        ccn3?.let {
            componentScope().launch(coroutineExceptionHandler) {
                val bird = api.getCountryByCode(it)
                _country.update { bird }
            }.invokeOnCompletion {
                _isLoading.update { false }
            }
        } ?: run {
            _isLoading.update { false }
            _noDetailsFoundError.update { true }
        }
    }

    fun onEvent(event: CountryDetailScreenEvent) {
        when(event) {
            CountryDetailScreenEvent.GoBack -> onNavigateBack()
            CountryDetailScreenEvent.RetryFetchDetails -> fetchCountryDetails()
        }
    }
}