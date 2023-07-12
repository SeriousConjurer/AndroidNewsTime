package com.example.newsapp2.ui.recyclerView

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp2.model.Article
import com.example.newsapp2.databinding.NewsCardBinding


class RecyclerViewAdapter(private val category: String) :
    RecyclerView.Adapter<RecyclerViewAdapter.ArticleViewHolder>() {
    private lateinit var binding: NewsCardBinding

    inner class ArticleViewHolder(val binding: NewsCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) {
//            Log.d("abc","bind()")
            binding.tvSource.text = article.source?.name
            binding.tvTitle.text = article.title
            binding.tvDescription.text = article.description
            val date = article.publishedAt?.take(10)
            val time = article.publishedAt?.takeLast(9)?.reversed()

            binding.tvPublishedAt.text = date
            binding.root.setOnClickListener {
                val urlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
                it.context.startActivity(urlIntent)
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
//            Log.d("abc","dare same()")
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
//            Log.d("abc","are contents()")
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
//        Log.d("abc","onCreate()")
        binding = NewsCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(
            binding
        )
    }

    override fun getItemCount(): Int {
//        Log.d("abc","getItemCount()")
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
//        Log.d("abc","onbind()")
        val article = differ.currentList[position]
        holder.bind(article)
        Glide.with(holder.itemView)
            .load(article.urlToImage)
            .into(holder.binding.ivImageArticle)
    }

}


// this is the reference code


//class RecyclerViewAdapter(private val articles : List<Article>) : RecyclerView.Adapter<NewsViewHolder>() {
//    private lateinit var binding : NewsCardBinding
//    //inner class NewsViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
//        binding = NewsCardBinding.inflate(LayoutInflater.from(parent.context))
//        val layoutInflater = LayoutInflater.from(parent.context)
//        val listItem = layoutInflater.inflate(R.layout.news_card,parent,false)
//        return NewsViewHolder(listItem)
//    }
//
//    override fun getItemCount(): Int {
//        return articles.size
//    }
//
//    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
//        val article = articles[position]
//        holder.bind(article)
//    }
//
//
////
////    private val differCallback = object : DiffUtil.ItemCallback<Article>(){
////        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
////            return oldItem.url == newItem.url
////        }
////
////        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
////            return oldItem == newItem
////        }
////
////    }
////    val differ = AsyncListDiffer(this,differCallback)
////
////    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
////        //binding = NewsCardBinding.inflate(LayoutInflater.from(parent.context))
////        return NewsViewHolder(
////            LayoutInflater.from(parent.context).inflate(R.layout.news_card,parent,false)
////        )
////    }
////
////    override fun getItemCount(): Int {
////        return differ.currentList.size
////    }
////
////    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
////        val article = differ.currentList[position]
////        holder.itemView.apply {
////            Glide.with(this).load(article.urlToImage).into(binding.ivArticleImage)
////            binding.tvSource.text = article.source.name
////            binding.tvTitle.text = article.title
////            binding.tvDescription.text = article.description
////            binding.tvPublishedAt.text = article.publishedAt
////            setOnClickListener{
////                onItemClickListener?.let { it(article) }
////            }
////        }
////    }
////    private var onItemClickListener:((Article) -> Unit)? = null
////
////    fun setOnItemClickListener(listener:(Article)->Unit){
////        onItemClickListener = listener
////    }
//
//
//}
//
//class NewsViewHolder(private val view:View) : ViewHolder(view){
//
//    private var onItemClickListener:((Article) -> Unit)? = null
//
//    fun setOnItemClickListener(listener:(Article)->Unit){
//        onItemClickListener = listener
//    }
//    fun bind(article:Article){
////        Log.d("baaaa" , article.title)
//        Glide.with(view).load(article.urlToImage).into(view.findViewById(ivArticleImage))
//        view.findViewById<TextView>(R.id.tvSource).text = article.source.name
//        view.findViewById<TextView>(R.id.tvTitle).text = article.title
//        view.findViewById<TextView>(R.id.tvDescription).text = article.description
//        view.findViewById<TextView>(R.id.tvPublishedAt).text = article.publishedAt
//        view.setOnClickListener{
//            val urlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
//            view.context.startActivity(urlIntent)
//        }
//    }
//}
