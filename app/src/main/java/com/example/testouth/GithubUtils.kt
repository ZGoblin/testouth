package com.example.testouth

import android.net.Uri
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GithubUtils {

  private companion object {
    const val clientId = "eb3c99e036920ec13d29"
    const val clientSecret = "11db94ae53e6bbba4a298c7742b56d5530969c76"
    const val redirectUrl = "githubproject://callback"
    const val scopes = "repo, user"
    const val schema = "https"
    const val host = "github.com"
  }

  private val retrofit by lazy {
    Retrofit.Builder()
        .client(
            OkHttpClient().newBuilder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
        )
        .addConverterFactory(GsonConverterFactory.create())
  }

  private val githubServiceOauth: GitHubService by lazy {
    retrofit
        .baseUrl(HttpUrl.Builder().scheme(schema).host(host).build())
        .build()
        .create(GitHubService::class.java)
  }
    private val githubService: GitHubService by lazy { retrofit
        retrofit
            .baseUrl(HttpUrl.Builder().scheme(schema).host("api.github.com").build())
            .build()
            .create(GitHubService::class.java)
    }

  fun buildAuthGitHubUrl(): Uri {
    return Uri.Builder()
        .scheme(schema)
        .authority(host)
        .appendEncodedPath("login/oauth/authorize")
        .appendQueryParameter("client_id", clientId)
        .appendQueryParameter("scope", scopes)
        .appendQueryParameter("redirect_url", redirectUrl)
        .build()
  }

  fun getCodeFromUri(uri: Uri?): String? {
    uri ?: return null
    if (!uri.toString().startsWith(redirectUrl)) {
      return null
    }
    return uri.getQueryParameter("code")
  }

  suspend fun getAccesToken(code: String): AccessToken {
    return githubServiceOauth.getAccessToken(clientId, clientSecret, code)
  }

  suspend fun getUser(token: String): User {
    return githubService.getUser(token)
  }

    suspend fun getRepos(token: String): List<Repos> {
        return githubService.getRepos(token)
    }

    suspend fun getContributors(token: String): List<User> {
        return githubService.getContributors(token, "github_project", "ZGoblin")
    }
}