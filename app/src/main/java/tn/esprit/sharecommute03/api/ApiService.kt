package tn.esprit.sharecommute03.api
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiService {
    @POST("/user/check-email-exists")
    suspend fun checkEmailExists(@Body email: String): Response<Unit>
    @POST("/user/signup")
    suspend fun signup(@Body user: UserRequest): Response<UserResponse>

    @POST("/user/signin")
    suspend fun signin(@Body user: UserRequest) : Response<UserResponse>
}