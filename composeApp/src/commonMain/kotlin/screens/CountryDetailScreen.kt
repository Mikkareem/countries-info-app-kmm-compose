package screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import screenevents.CountryDetailScreenEvent
import screens.components.NetworkAwareContainer
import viewmodels.CountryDetailComponent

@Composable
fun CountryDetailScreen(
    component: CountryDetailComponent
) {
    val country by component.country.subscribeAsState()
    val loading by component.isLoading.subscribeAsState()
    val internetError by component.networkError.subscribeAsState()

    Box(
        modifier = Modifier.fillMaxSize().background(color = Color(0xFFAA336A)).padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        NetworkAwareContainer(
            loading = loading,
            internetError = internetError,
            onRetry = { component.onEvent(CountryDetailScreenEvent.RetryFetchDetails) }
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                CompositionLocalProvider(LocalTextStyle provides LocalTextStyle.current.copy(color = Color.White, fontSize = 20.sp)) {

                }
                Button(onClick = { component.onEvent(CountryDetailScreenEvent.GoBack) }) {
                    Text("Go Back")
                }
            }
        }
    }
}