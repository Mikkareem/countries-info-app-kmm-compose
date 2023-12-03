import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import navigation.RootComponent
import screens.CountriesListScreen
import screens.CountryDetailScreen

@Composable
fun App(root: RootComponent) {

    val childStack by root.childStack.subscribeAsState()

    MaterialTheme {
        Children(
            stack = childStack,
            animation = stackAnimation(slide())
        ) { child ->
            when(val instance = child.instance) {
                is RootComponent.Child.CountriesListScreen -> CountriesListScreen(instance.component)
                is RootComponent.Child.CountryDetailScreen -> CountryDetailScreen(instance.component)
            }
        }
    }
}