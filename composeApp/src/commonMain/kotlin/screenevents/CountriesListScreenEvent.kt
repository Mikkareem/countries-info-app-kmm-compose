package screenevents

sealed interface CountriesListScreenEvent {
    data class ClickOnCountry(val ccn3: String?): CountriesListScreenEvent
    data object RetryFetch: CountriesListScreenEvent
}