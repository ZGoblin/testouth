package com.example.testouth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pm.tech.myapplication.data.pref.SharedPref

class MainActivity : AppCompatActivity() {

    private val sharedPreferences by lazy {
        SharedPref(this)
    }

    private val githubUtils: GithubUtils by lazy {
        GithubUtils()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.login).setOnClickListener {
            startGitHubLogin()
        }
    }

    private fun startGitHubLogin() {
        val authIntent = Intent(Intent.ACTION_VIEW, githubUtils.buildAuthGitHubUrl())
        startActivity(authIntent)
    }

    override fun onResume() {
        super.onResume()

        val code = githubUtils.getCodeFromUri(uri = intent.data)
        code ?: return

        GlobalScope.launch {
            val response = githubUtils.getAccesToken(code)
            val token = "${response.tokenType} ${response.accessToken}"
            sharedPreferences.token = token
            Log.d("TAG_11", "token $token")
            val user = githubUtils.getUser(token)
            val repos = githubUtils.getRepos(token)
            val contr = githubUtils.getContributors(token)

            Log.d("TAG_11", "user name ${user.name}")
            Log.d("TAG_11", "user avatar ${user.avatar_url}")
            Log.d("TAG_11", "user repos ${repos}")
            Log.d("TAG_11", "repo contr ${contr}")
        }
    }
}