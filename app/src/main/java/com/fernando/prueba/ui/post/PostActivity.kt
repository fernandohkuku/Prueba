package com.fernando.prueba.ui.post

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ScrollView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.fernando.prueba.R
import com.fernando.prueba.databinding.ActivityPostBinding
import com.fernando.prueba.interfaces.PostListener
import com.fernando.prueba.interfaces.ResponseListener
import com.fernando.prueba.models.Post
import com.fernando.prueba.ui.post.adapters.PostAdapter
import com.fernando.prueba.utils.ApiServiceException
import com.fernando.prueba.utils.Coroutines
import com.fernando.prueba.utils.NoInternetException
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.lang.Exception

class PostActivity : AppCompatActivity(R.layout.activity_post), KodeinAware, ResponseListener, PostListener {

    override val kodein by kodein()

    private val binding: ActivityPostBinding by viewBinding();

    private val postViewModelFactory:PostViewModelFactory by instance();

    private lateinit var postViewModel: PostViewModel


    private var postAdapter:PostAdapter?=null;


    private var isFirstLoad = true;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onStarted()
        this.postViewModel = ViewModelProvider(this, postViewModelFactory).get(PostViewModel::class.java);

        this.postAdapter = PostAdapter(applicationContext)

        this.postViewModel.getPosts().observe(this, {posts->
            if (posts.isNotEmpty()){
                onSuccess()
                if(isFirstLoad){
                    initRecyclerView(posts)
                    isFirstLoad = false;
                }

            }else{
                getPosts()
            }
        })
    }

    private fun getPosts() = Coroutines.io {
        try {
            var posts  = postViewModel.fetchPosts();
            posts?.forEachIndexed { index, post ->
                post.isRead = index <= 19
            }
            postViewModel.savePosts(posts!!);
        }catch (e:ApiServiceException){
            onFailure(e.message!!)
        }catch (e:NoInternetException){
            onFailure(e.message!!)
        }catch (e:Exception){
            onFailure(e.message!!)
        }
    }

    private fun initRecyclerView(posts: List<Post>){
        this.binding.rcvPost.layoutManager = LinearLayoutManager(applicationContext);
        this.binding.rcvPost.setHasFixedSize(false);
        this.binding.rcvPost.isNestedScrollingEnabled = false;
        this.binding.rcvPost.descendantFocusability = ScrollView.FOCUS_BEFORE_DESCENDANTS;
        this.postAdapter?.setData(posts, this)
        this.binding.rcvPost.adapter = this.postAdapter

    }

    override fun onStarted() {
        this.binding.progressBar.visibility = View.VISIBLE;
    }

    override fun onFailure(message:String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSuccess() {
        this.binding.progressBar.visibility = View.GONE;
    }

    override fun onSelectedPost(post: Post, position: Int) {
        Toast.makeText(applicationContext, post.title, Toast.LENGTH_SHORT).show()
        Coroutines.main {
            postViewModel.readPost(false, post.id!!)
            this.postAdapter?.removeItem(position);
            this.postAdapter?.restoreItem(post, position)
        }
    }
}