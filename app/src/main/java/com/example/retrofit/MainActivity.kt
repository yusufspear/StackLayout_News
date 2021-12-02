package com.example.retrofit

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retrofit.adapter.MyAdapter
import com.example.retrofit.constant.Constant
import com.example.retrofit.databinding.ActivityMainBinding
import com.example.retrofit.model.Article
import com.example.retrofit.model.News
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.littlemango.stacklayoutmanager.StackLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var retrofit: RetrofitInterface
    lateinit var binding: ActivityMainBinding
    lateinit var stringBuilder: StringBuilder
    lateinit var list: ArrayList<Article>
    lateinit var layoutManager: LinearLayoutManager
    lateinit var stackLayoutManager: StackLayoutManager
    var totalResults :Int = 0
    var page :Int = 1
    //AdMob
    private var mInterstitialAd: InterstitialAd? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //AdMob
        MobileAds.initialize(this)

        var adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                d("TAG", adError?.message)
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                d("TAG", "Ad was loaded.")
                mInterstitialAd = interstitialAd

            }
        })



        retrofit = RetrofitApi.getInstance(applicationContext)
        stringBuilder = StringBuilder()

        list = ArrayList<Article>()


        stackLayoutManager =
            StackLayoutManager(StackLayoutManager.ScrollOrientation.BOTTOM_TO_TOP)
        stackLayoutManager.setPagerMode(true)
        stackLayoutManager.setPagerFlingVelocity(2000)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerview.layoutManager = stackLayoutManager
        binding.recyclerview.adapter = MyAdapter(applicationContext, list)
        loadData(page)

        stackLayoutManager.setItemChangedListener(object : StackLayoutManager.ItemChangedListener {
            override fun onItemChanged(position: Int) {
                v("MainActivity", "Position  : $position")
                v("MainActivity", "List Size   : $list.size")
                if (position==list.size-1 && position<totalResults && list.size!=totalResults){
                    loadData(++page)
                    v("MainActivity", "page   : $page")

                }
                if(position%5==0){
                    if (mInterstitialAd != null) {
                        mInterstitialAd?.show(this@MainActivity)
                    } else {
                        d("TAG", "The interstitial ad wasn't ready yet.")
                    }
                }
            }
        })

    }

    private fun loadData(nextPage: Int) {
        retrofit.getNews(Constant().getapiKey(), "in", nextPage).enqueue(object : Callback<News?> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<News?>, response: Response<News?>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        applicationContext,
                        response.body()!!.totalResults.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    list.addAll(response.body()!!.articles)
                    totalResults=response.body()!!.totalResults
                    v("List", "Total Result : " + totalResults.toString())


                    v("List", "Size of List is : " + list.size.toString())

                    binding.recyclerview.adapter?.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<News?>, t: Throwable) {
                e("TAG", t.message.toString())
            }
        })
    }


//
//    private fun getAllPost() {
//
//        retrofit.getData().enqueue(object : Callback<List<Post>?> {
//            override fun onResponse(call: Call<List<Post>?>, response: Response<List<Post>?>) {
//                if (response.isSuccessful) {
//                    for (post in response.body()!!) {
//                        showPost(post)
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<List<Post>?>, t: Throwable) {
//                Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//
//    fun run(view: View) {
////        getAllPost()
//        getSinglePost()
//        getPostByQuery()
////        postSingle()
////        postMultiple()
//    }
//
//    private fun postMultiple() {
//        val list = mutableListOf<Post>()
//        list.add(Post(5, null, "This is Title", "This is Body"))
//        list.add(Post(4, null, "This is Title2", "This is Body2"))
//        list.add(Post(3, null, "This is Title3", "This is Body3"))
//        list.add(Post(2, null, "This is Title4", "This is Body4"))
//        list.add(Post(1, null, "This is Title5", "This is Body5"))
//        retrofit.createMultiplePost(list)
//            .enqueue(object : Callback<MutableList<Post>?> {
//                override fun onResponse(
//                    call: Call<MutableList<Post>?>,
//                    response: Response<MutableList<Post>?>
//                ) {
//                    if (response.isSuccessful) {
//                        v("TAG", response.headers().size.toString())
//                        for (post in response.body()!!) {
//                            showPost(post)
//                        }
//                    }
//                }
//
//                override fun onFailure(call: Call<MutableList<Post>?>, t: Throwable) {
//                    e("TAG", t.message.toString())
//                }
//            })
//    }
//
//    private fun postSingle() {
//        retrofit.createSinglePost(Post(5, null, "This is Title", "This is Body"))
//            .enqueue(object : Callback<Post?> {
//                override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
//                    if (response.isSuccessful) {
//                        showPost(response.body()!!)
//                    }
//                }
//
//                override fun onFailure(call: Call<Post?>, t: Throwable) {
//                    TODO("Not yet implemented")
//                }
//            })
//    }
//
//    private fun getPostByQuery() {
//        retrofit.getPostByQuery(5).enqueue(object : Callback<List<Comment>?> {
//            override fun onResponse(
//                call: Call<List<Comment>?>,
//                response: Response<List<Comment>?>
//            ) {
//                if (response.isSuccessful) {
//                    v("TAG", response.headers().size.toString())
//                    for (comment in response.body()!!) {
//                        showComment(comment)
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<List<Comment>?>, t: Throwable) {
//                TODO("Not yet implemented")
//            }
//        })
//    }
//
//    private fun getSinglePost() {
//        retrofit.getPostById(5).enqueue(object : Callback<Post?> {
//            override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
//
//                if (response.isSuccessful) {
//                    showPost(response.body()!!)
//
//                }
//            }
//
//            override fun onFailure(call: Call<Post?>, t: Throwable) {
//                Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
//
//            }
//        })
//    }
//
//
//    fun showPost(post: Post) {
//        stringBuilder.append("PostId: " + post.userId + "\n")
//        stringBuilder.append("Id: " + post.id + "\n")
//        stringBuilder.append("title: " + post.title + "\n")
//        stringBuilder.append("body: " + post.body + "\n\n")
////        binding.tv.text = stringBuilder.toString()
//    }
//
//    fun showComment(comment: Comment) {
//        stringBuilder.append("PostId: " + comment.postId + "\n")
//        stringBuilder.append("Id: " + comment.id + "\n")
//        stringBuilder.append("name: " + comment.name + "\n")
//        stringBuilder.append("email: " + comment.email + "\n")
//        stringBuilder.append("body: " + comment.body + "\n\n")
////        binding.tv.text = stringBuilder.toString()
//    }

}

