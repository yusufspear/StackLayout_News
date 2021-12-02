package com.example.retrofit


import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("postId")
    val postId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("body")
    val body: String
)
data class Post(
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("title")
    val title: String,
    @SerializedName("body")
    val body: String
)