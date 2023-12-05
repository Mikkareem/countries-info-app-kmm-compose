package screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import screenevents.CountriesListScreenEvent
import screens.components.NetworkAwareContainer
import viewmodels.CountriesListComponent

@Composable
fun CountriesListScreen(
    component: CountriesListComponent
) {
    val countries by component.countries.subscribeAsState()

    val internetError by component.networkError.subscribeAsState()
    val loading by component.isLoading.subscribeAsState()

    Box(
        modifier = Modifier.fillMaxSize().background(color = Color(0xFFBF40BF)),
        contentAlignment = Alignment.Center
    ) {
        NetworkAwareContainer(
            loading = loading,
            internetError = internetError,
            onRetry = { component.onEvent(CountriesListScreenEvent.RetryFetch) }
        ) {
            Column {
                Text(
                    "Countries",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(top = 16.dp, start = 16.dp)
                )
                LazyColumn {
                    items(countries) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    component.onEvent(CountriesListScreenEvent.ClickOnCountry(it.ccn3))
                                }
                                .padding(16.dp)
                        ) {
                            it.flags?.let { flag ->
                                KamelImage(
                                    resource = asyncPainterResource(data = flag.url),
                                    contentDescription = null,
                                    modifier = Modifier.size(100.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(it.name.common, fontSize = 24.sp, color = Color.White)
                                Text(it.name.official, fontSize = 16.sp, color = Color.White)
                            }
                        }
                        Divider()
                    }
                }
            }
        }
    }
}