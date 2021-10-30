package edu.gwu.androidnews

import android.util.Base64
import android.util.Log
import android.widget.Toast
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


    fun retrieveArticles(apiKey: String, query: String, source: String = ""): List<Article> {
        val articles: MutableList<Article> = mutableListOf()
        var url = ""
        if (source.isBlank()) {
            url = "https://newsapi.org/v2/everything?language=en&q=$query&apiKey=$apiKey"
        } else {
            url = "https://newsapi.org/v2/everything?language=en&sources=$source&q=$query&apiKey=$apiKey"
        }
        val request: Request = Request.Builder()
            .url(url)
            .build()

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


    fun retrieveArticlesInTitle(apiKey: String, location: String): List<Article> {
        val articles: MutableList<Article> = mutableListOf()

        val request: Request = Request.Builder()
            .url("https://newsapi.org/v2/everything?qInTitle=$location&apiKey=$apiKey")
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

    fun retrieveHeadlines(apiKey: String, category: String, page: Int): List<Article> {
        val articles: MutableList<Article> = mutableListOf()

        val request: Request = Request.Builder()
            .url("https://newsapi.org/v2/top-headlines?country=us&category=$category&page=$page&apiKey=$apiKey")
            .build()

        val response: Response = okHttpClient.newCall(request).execute()
        val responseBody: String? = response.body?.string()

        if (response.isSuccessful && !responseBody.isNullOrBlank()) {
            Log.d("HeadlinesActivity", "Successful response")
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

    fun retrieveSources(apiKey: String, category: String): List<Source> {
        val sources: MutableList<Source> = mutableListOf()

        val request: Request = Request.Builder()
            .url("https://newsapi.org/v2/top-headlines/sources?language=en&category=$category&apiKey=$apiKey")
            .build()
        Log.d("MapsActivity", "set get https request")

        val response: Response = okHttpClient.newCall(request).execute()
        val responseBody: String? = response.body?.string()

        if (response.isSuccessful && !responseBody.isNullOrBlank()) {
            Log.d("SourceActivity", "Successful response")
            val json = JSONObject(responseBody)
            val sourcesArr: JSONArray = json.getJSONArray("sources")

            for (i in 0 until sourcesArr.length()) {
                val curr: JSONObject = sourcesArr.getJSONObject(i)

                val name: String = curr.getString("name")
                val description: String = curr.getString("description")
                val id: String = curr.getString("id")

                val source = Source(
                    name = name,
                    description = description,
                    id =id
                )

                sources.add(source)
            }
        }

        return sources
    }
}