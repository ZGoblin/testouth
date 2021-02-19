package com.example.testouth

import retrofit2.http.*

interface GitHubService {

  @Headers("Accept: application/json")
  @POST("login/oauth/access_token")
  @FormUrlEncoded
  suspend fun getAccessToken(
      @Field("client_id") clientId: String,
      @Field("client_secret") clientSecret: String,
      @Field("code") code: String,
  ): AccessToken

  @Headers("Accept: application/vnd.github.v3+json")
  @GET("/user")
  suspend fun getUser(@Header("Authorization") auth: String): User

  @Headers("Accept: application/vnd.github.v3+json")
  @GET("/user/repos")
  suspend fun getRepos(@Header("Authorization") auth: String): List<Repos>
}
