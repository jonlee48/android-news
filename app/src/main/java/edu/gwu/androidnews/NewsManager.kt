package edu.gwu.androidnews

import android.util.Base64
import android.util.Log
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLEncoder

class NewsManager {
    val okHttpClient: OkHttpClient

    init {
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        okHttpClientBuilder.addInterceptor(loggingInterceptor)

        okHttpClient = okHttpClientBuilder.build()
    }


    fun retrieveArticles(apiKey: String, location: String): List<Article> {
        val articles: MutableList<Article> = mutableListOf()

        val request: Request = Request.Builder()
            .url("https://newsapi.org/v2/everything?q=$location&apiKey=$apiKey")
            .build()
        Log.d("MapsActivity", "test")

        val response: Response = okHttpClient.newCall(request).execute()
        val responseBody: String? = response.body?.string()

        if (response.isSuccessful && !responseBody.isNullOrBlank()) {
            Log.d("MapsActivity", "Successful response")
            val json: JSONObject = JSONObject(responseBody)
            val articlesArr: JSONArray = json.getJSONArray("articles")

            for (i in 0 until articlesArr.length()) {
                val curr: JSONObject = articlesArr.getJSONObject(i)

                val sourceObj: JSONObject = curr.getJSONObject("source")
                val source: String = sourceObj.getString("name")
                val title: String = curr.getString("title")
                val content: String = curr.getString("description")
                val iconURL: String = curr.getString("urlToImage")
                val url: String = curr.getString("url")

                val article: Article = Article(
                    title = title,
                    source = source,
                    content = content,
                    iconUrl = iconURL,
                    link = url
                )

                articles.add(article)
            }
        }

        return articles
    }
}