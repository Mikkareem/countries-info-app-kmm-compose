package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

@Serializable
data class Country(
    val name: CountryName = CountryName(),
    val tld: List<String> = emptyList(),
    val independent: Boolean? = null,
    val status: String = "",
    @SerialName("unMember")
    val UNMember: Boolean = false,
    @Serializable(with = CurrenciesSerializer::class)
    val currencies: List<Currencies> = emptyList(),
    val capital: List<String> = emptyList(),
    val ccn3: String? = null,
    val flags: Flag? = null
)

@Serializable
data class Currencies(
    val name: String,
    val currency: Currency
)

@Serializable
data class Currency(
    val name: String,
    val symbol: String? = null
)


@Serializable
data class CountryName(
    val common: String = "",
    val official: String = "",
    val nativeName: LanguageName? = null
)

@Serializable
data class LanguageName(
    val eng: CountryName? = null,
    val hin: CountryName? = null,
    val tam: CountryName? = null
)

@Serializable
data class Flag(
    val png: String? = null,
    val svg: String? = null
) {
    val url = png ?: svg ?: ""
}

private object CurrenciesSerializer: JsonTransformingSerializer<List<Currencies>>(ListSerializer(Currencies.serializer())) {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        require(element is JsonObject)
        val keys = element.keys
        val newElement = buildJsonArray {
            keys.forEach {
                add(
                    buildJsonObject {
                        put("name", it)
                        put("currency", element[it]!!)
                    }
                )
            }
        }
        return newElement
    }
}