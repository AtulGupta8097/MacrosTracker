package com.example.responsiveapp.data.remote.dto.fatsecret.detail
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type
@JsonAdapter(ServingsContainerDto.Deserializer::class)
data class ServingsContainerDto(
    @SerializedName("serving")
    val serving: List<ServingDto>
) {
    class Deserializer : JsonDeserializer<ServingsContainerDto> {
        override fun deserialize(
            json: JsonElement,
            typeOfT: Type,
            context: JsonDeserializationContext
        ): ServingsContainerDto {
            val servingElement = json.asJsonObject.get("serving")

            val servings = when {
                servingElement.isJsonArray -> {
                    servingElement.asJsonArray.map {
                        context.deserialize<ServingDto>(it, ServingDto::class.java)
                    }
                }

                servingElement.isJsonObject -> {
                    listOf(context.deserialize<ServingDto>(servingElement, ServingDto::class.java))
                }
                else -> emptyList()
            }

            return ServingsContainerDto(servings)
        }
    }
}