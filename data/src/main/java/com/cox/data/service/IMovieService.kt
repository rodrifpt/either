package com.cox.data.service

import com.cox.domain.model.UpcomingResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface IMovieService {
    @GET("upcoming")
    suspend fun getUpcoming(
        @Query("api_key") apiKey: String = "8f9f82e38c2b7c4b9ffda05f84747c4c",
        @Query("language") language: String = "en-US",
        @Query("page") page: Int
    ): Response<UpcomingResponse>
}
