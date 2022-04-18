package com.drury.topredditposts.presentation.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.drury.topredditposts.R
import com.drury.topredditposts.databinding.RedditPostViewBinding
import com.drury.topredditposts.domain.model.RedditPost

class TopRedditPostAdapter(
    private val onClickRedditPostDetails : (redditPost : RedditPost) -> Unit,
    private val shoulShowDetails : (redditPost : RedditPost) -> Boolean
) : RecyclerView.Adapter<TopRedditPostAdapter.RedditPostViewHolder>() {

    private val items : MutableList<RedditPost> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RedditPostViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val bindingRedditPost = RedditPostViewBinding.inflate(layoutInflater)

        return RedditPostViewHolder(bindingRedditPost)
    }

    override fun onBindViewHolder(holder: RedditPostViewHolder, position: Int) {
        holder.bind(item = items[position], onClickRedditPostDetails, shoulShowDetails)
    }

    override fun getItemCount(): Int {
        return items.size
    }


    fun setItems(newItems : List<RedditPost>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun addItems(newItems : List<RedditPost>) {
        val lastPosition = items.size - 1
        items.addAll(newItems)
        notifyItemRangeChanged(lastPosition, items.size)
    }
    fun getLastItemId() : String? {
        return items.last().name
    }

    class RedditPostViewHolder(
        val binding: RedditPostViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RedditPost, onClickRedditPostDetails : (redditPost : RedditPost) -> Unit, shoulShowDetails : (redditPost : RedditPost) -> Boolean) {
            // Only show Reddit details if the thumbnail is JPG or PNG
            binding.redditPostDetailsButton.visibility = if (shoulShowDetails(item)) View.VISIBLE else View.GONE
            binding.redditPostDetailsButton.setOnClickListener {
                onClickRedditPostDetails(item)
            }
            Glide.with(itemView)
                .load(item.thumbnail)
                .centerCrop()
                .placeholder(R.drawable.ic_image_placeholder)
                .into(binding.redditThumbnail)
            binding.redditPostName.text = item.title
        }
    }
}