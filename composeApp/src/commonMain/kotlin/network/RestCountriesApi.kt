package network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import model.Country

interface RestCountriesApi {
    suspend fun getAllCountries(): List<Country>
    suspend fun getCountryByCode(ccn3: String): Country
    suspend fun getCountryByName(name: String): Country
}

internal class RestCountriesApiImpl(
    private val client: HttpClient
): RestCountriesApi {
    override suspend fun getAllCountries(): List<Country> {
        return withContext(Dispatchers.IO) {
            val response = client.get("https://restcountries.com/v3.1/all")
            val countries = response.body<List<Country>>()
            countries
        }
    }

    override suspend fun getCountryByCode(ccn3: String): Country {
        return withContext(Dispatchers.IO) {
            val response = client.get("https://restcountries.com/v3.1/alpha/${ccn3}")
            val countries = response.body<List<Country>>()
            countries.first()
        }
    }

    override suspend fun getCountryByName(name: String): Country {
        return withContext(Dispatchers.IO) {
            val response = client.get("https://restcountries.com/v3.1/name/${name}?fullText=true")
            val countries = response.body<List<Country>>()
            countries.first()
        }
    }
}