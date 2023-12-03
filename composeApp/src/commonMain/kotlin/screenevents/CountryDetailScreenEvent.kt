package screenevents

sealed interface CountryDetailScreenEvent {
    data object GoBack: CountryDetailScreenEvent
    data object RetryFetchDetails: CountryDetailScreenEvent
}