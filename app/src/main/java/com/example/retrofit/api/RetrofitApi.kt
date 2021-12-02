package com.example.retrofit

import android.content.Context
import com.example.retrofit.constant.Constant
import com.example.retrofit.model.News
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


abstract class RetrofitApi {


    companion object {
        private var instance: RetrofitInterface? = null


        fun getInstance(context: Context): RetrofitInterface {

            if (instance == null) {
                synchronized(this) {
                    val logger =
                        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                    val client = OkHttpClient.Builder().addInterceptor(logger).build()

                    val retrofit = Retrofit.Builder().baseUrl(Constant().URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(client)
                        .build()

                    instance = retrofit.create(RetrofitInterface::class.java)
                    return instance!!
                }

            }
            return instance!!


        }


    }

}


interface RetrofitInterface {

    //Simple GET request
    @GET("posts")
    fun getData(): Call<List<Post>>

    //Dynamic GET request
    @GET("posts/{id}")
    fun getPostById(@Path("id") id: Int): Call<Post>

    //Query GET request
    @GET("comments")
    fun getPostByQuery(@Query("postId") id: Int): Call<List<Comment>>

    @POST("posts")
    fun createSinglePost(@Body post: Post): Call<Post>

    //Don't Use This Because You Need No of Class To Store Return Data
    /*{
        "0": {
        "body": "This is Body",
        "title": "This is Title",
        "userId": 5
    },
        "1": {
        "body": "This is Body2",
        "title": "This is Title2",
        "userId": 4
    },
        "id": 101
    }*/
    @POST("posts")
    fun createMultiplePost(@Body post: List<Post>): Call<MutableList<Post>>

    @FormUrlEncoded
    @POST("posts")
    fun createPostByURL(
        @Field("postId") id: Int, @Field("title") title: String, @Field("body") body: String
    ): Call<Post>

    @PUT("posts/{id}")
    fun updatePostByPut(@Path("id") id: Int, @Body post: Post): Call<Post>

    @PATCH("posts/{id}")
    fun updatePostByPatch(@Path("id") id: Int, @Body post: Post): Call<Post>

    @DELETE("posts/{id}")
    fun deletePost(@Path("id") id: Int, @Body post: Post): Call<Void>


    @GET("top-headlines")
    fun getNews(
        @Query("apiKey") key: String,
        @Query("country") country: String,
        @Query("page") page: Int
    ): Call<News>

}
