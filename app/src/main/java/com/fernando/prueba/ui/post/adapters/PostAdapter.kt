package com.fernando.prueba.ui.post.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fernando.prueba.R
import com.fernando.prueba.databinding.ItemPostBinding
import com.fernando.prueba.interfaces.PostListener
import com.fernando.prueba.models.Post

class PostAdapter(
    private val context: Context
):RecyclerView.Adapter<PostAdapter.PostHolder>() {

    private lateinit var listener: PostListener
    private var mData = mutableListOf<Post>()

    internal fun setData(data:List<Post>, listener: PostListener){
        this.mData = data as MutableList<Post>;
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapter.PostHolder {
        ItemPostBinding.inflate(LayoutInflater.from(context), parent, false).also {binding->
            return  PostHolder(binding);
        }
    }

    override fun onBindViewHolder(holder: PostAdapter.PostHolder, position: Int) {
        val post = this.mData[position];
        holder.bind(post, position)
    }

    override fun getItemCount() = this.mData.size

    internal fun removeItem(position: Int){
        var post:Post = this.mData[position];
        listener.onDeletePost(post, position)
        this.mData.removeAt(position)
        notifyItemRemoved(position)
    }

    internal fun updateItem(post: Post, position: Int){
        post.isRead = false;
        this.mData.add(position, post);
        notifyItemChanged(position)
    }

    internal fun restoreItem(post: Post, position: Int){
        post.isRead = false;
        this.mData.add(position, post)
        notifyItemInserted(position)
    }

    inner class PostHolder(val binding: ItemPostBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(post: Post, position: Int) {
            if (post.isRead!!){
                this.binding.textView
                    .setBackgroundColor(ContextCompat.getColor(context, R.color.blue_material_700))
            }else{
                this.binding.textView
                    .setBackgroundColor(ContextCompat.getColor(context, R.color.white))
            }
            if(post.isFavorite!!){
                this.binding.tvFavorite.setBackgroundResource(R.drawable.ic_star)
            }else{
                this.binding.tvFavorite.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
            }
            this.binding.tvTitle.text = post.title.toString()
            this.binding.tvBody.text = post.body.toString()
            this.binding.root.setOnClickListener {
                listener.onSelectedPost(post, position)
            }
        }

    }
}