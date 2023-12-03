package navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import kotlinx.serialization.Serializable
import viewmodels.CountriesListComponent
import viewmodels.CountryDetailComponent

class RootComponent(
    componentContext: ComponentContext
): ComponentContext by componentContext {
    private val navigation = StackNavigation<Configuration>()

    val childStack = childStack(
        source = navigation,
        handleBackButton = true,
        childFactory = ::createChild,
        serializer = Configuration.serializer(),
        initialConfiguration = Configuration.CountriesListScreen
    )

    @OptIn(ExperimentalDecomposeApi::class)
    private fun createChild(
        config: Configuration,
        context: ComponentContext
    ): Child {
        return when(config) {
            is Configuration.CountriesListScreen -> Child.CountriesListScreen(
                component = CountriesListComponent(
                    componentContext = context,
                    onNavigateToCountryDetailScreen = { navigation.pushNew(Configuration.CountryDetailScreen(it)) }
                )
            )
            is Configuration.CountryDetailScreen -> Child.CountryDetailScreen(
                component = CountryDetailComponent(
                    ccn3 = config.ccn3,
                    componentContext = context,
                    onNavigateBack = { navigation.pop() }
                )
            )
        }
    }

    sealed class Child {
        data class CountriesListScreen(val component: CountriesListComponent): Child()
        data class CountryDetailScreen(val component: CountryDetailComponent): Child()
    }

    @Serializable
    sealed class Configuration {
        @Serializable
        data object CountriesListScreen: Configuration()
        @Serializable
        data class CountryDetailScreen(val ccn3: String?): Configuration()
    }
}