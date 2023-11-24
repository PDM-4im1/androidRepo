package tn.esprit.sharecommunity.drivers.data

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

sealed class UserRole(val value: String) {
    object Admin : UserRole("admin")
    object Driver : UserRole("driver")
    object Client : UserRole("client")
    object DeliveryMan : UserRole("delivery man")

    class Deserializer : JsonDeserializer<UserRole> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): UserRole {
            return when (val roleValue = json?.asString) {
                Admin.value -> Admin
                Driver.value -> Driver
                Client.value -> Client
                DeliveryMan.value -> DeliveryMan
                else -> throw IllegalArgumentException("Unknown UserRole value: $roleValue")
            }
        }
    }
}