package com.example.retrofit.adapter

import android.content.Context
import android.content.Intent
import android.util.Log.v
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.retrofit.R
import com.example.retrofit.WebViewActivity
import com.example.retrofit.databinding.RawBinding
import com.example.retrofit.model.Article

class MyAdapter(private val context: Context, private val list:List<Article>): RecyclerView.Adapter<MyAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view=LayoutInflater.from(parent.context).inflate(R.layout.raw,parent,false)
        val binding=RawBinding.bind(view)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if(list[position].urlToImage != null)
            Glide.with(context).load(list[position].urlToImage).into(holder.binding.imageView)
        else
            Glide.with(context).load(R.drawable.no_image).into(holder.binding.imageView)

        holder.binding.textView.text=list[position].title
        holder.binding.textView2.text=list[position].description
        holder.itemView.setOnClickListener{
            val intent=Intent()
            intent.putExtra("URL",list[position].url)
            intent.setClass(context, WebViewActivity::class.java)
            intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        v("TAG","Size of List in Adapter "+list.size.toString())
        return list.size
    }

    inner class ViewHolder(val binding: RawBinding) :RecyclerView.ViewHolder(binding.root){

        init {

        }
    }
}