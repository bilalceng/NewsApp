package com.raywenderlich.newsapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.raywenderlich.newsapp.R
import com.raywenderlich.newsapp.models.Article
import retrofit2.http.Url

class ArticleAdapter : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_article_preview, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val currentArticle = differ.currentList[position]

        val image: ImageView = holder.itemView.findViewById(R.id.ivArticleImage)
        val source: TextView = holder.itemView.findViewById(R.id.tvSource)
        val publishAt: TextView = holder.itemView.findViewById(R.id.tvPublishedAt)
        val title: TextView = holder.itemView.findViewById(R.id.tvTitle)
        val description: TextView = holder.itemView.findViewById(R.id.tvDescription)


        Glide.with(holder.itemView).load(currentArticle.urlToImage).into(image)
        source.text = currentArticle.source!!.name
        publishAt.text = currentArticle.publishedAt
        title.text = currentArticle.title
        description.text = currentArticle.description

        holder.itemView.setOnClickListener {
            onToolClickListener?.let {
                it(currentArticle)
            }
        }



    }

    var onToolClickListener: ((Article) -> Unit)? = null

    fun setOnItemClickListener(listener: (Article) -> Unit){
        onToolClickListener = listener
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}