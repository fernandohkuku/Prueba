package com.fernando.prueba.ui.post

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ScrollView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.fernando.prueba.R
import com.fernando.prueba.databinding.ActivityPostBinding
import com.fernando.prueba.databinding.ActivityUserBinding
import com.fernando.prueba.interfaces.PostListener
import com.fernando.prueba.interfaces.ResponseListener
import com.fernando.prueba.models.Post
import com.fernando.prueba.ui.post.adapters.PostAdapter
import com.fernando.prueba.ui.user.UserActivity
import com.fernando.prueba.utils.ApiServiceException
import com.fernando.prueba.utils.Coroutines
import com.fernando.prueba.utils.NoInternetException
import com.fernando.prueba.utils.RecyclerItemTouchHelper
import kotlinx.coroutines.delay
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.lang.Exception

class PostActivity : AppCompatActivity(R.layout.activity_post),
    KodeinAware, ResponseListener, PostListener,
    RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, View.OnClickListener{

    override val kodein by kodein()

    private val binding: ActivityPostBinding by viewBinding();

    private val postViewModelFactory:PostViewModelFactory by instance();

    private lateinit var postViewModel: PostViewModel

    private var postAdapter:PostAdapter?=null;


    private var isDeleted = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onStarted()
        this.postViewModel = ViewModelProvider(this, postViewModelFactory).get(PostViewModel::class.java);

        this.postAdapter = PostAdapter(applicationContext)

        this.postViewModel.getPosts().observe(this, {posts->
            if (posts.isNotEmpty()){
                onSuccess()
            }else{
                if(!isDeleted){
                    getPosts()
                }
            }
            initRecyclerView(posts)
        })

        RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this).also {rcv->
            ItemTouchHelper(rcv).attachToRecyclerView(this.binding.rcvPost)
        }

        this.binding.btnRefresh.setOnClickListener(this)
        this.binding.btnDelete.setOnClickListener(this)
        this.binding.btnFilter.setOnClickListener(this)
    }

    private fun getPosts() = Coroutines.main {
        try {
            onStarted()
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
        this.binding.rcvPost.swapAdapter(this.postAdapter, true)
        this.postAdapter?.setData(posts, this)
        this.binding.rcvPost.adapter = this.postAdapter

    }

    private fun deleteAllPosts() = Coroutines.main {
        try{
            isDeleted = true;
            postViewModel.deleteAllPosts()
        }catch (e:Exception){
            Log.e("ROOM ERROR", e.message!!)
        }

    }

    private fun loadFragment(fragment:Fragment){
        supportFragmentManager.beginTransaction().also {transaction->
            transaction.replace(this.binding.container.id, fragment)
                .addToBackStack(null).commit()
        }
    }

    override fun onStarted() {
        this.binding.progressBar.visibility = View.VISIBLE;
    }

    override fun onFailure(message:String) = Coroutines.main {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSuccess() {
        this.binding.progressBar.visibility = View.GONE;
    }

    override fun onSelectedPost(post: Post, position: Int) {
        Coroutines.main {
            Intent(applicationContext, UserActivity::class.java).also { intent->
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("post", post)
                startActivity(intent)
                delay(100)
                postViewModel.readPost(false, post.id!!)
                this.postAdapter?.updateItem(post, position)
            }
        }
    }

    override fun onDeletePost(post: Post, position: Int) = Coroutines.main{
        try {
            postViewModel.deletePost(post!!.id!!)
            postViewModel.deletePost(post.id.toString()).also { post ->
                if(post!=null){
                    Toast.makeText(applicationContext, "the post was deleted", Toast.LENGTH_SHORT).show()
                }
            }
        }catch (e:ApiServiceException){

        }catch (e:NoInternetException){

        }catch (e:Exception){
            Log.d("ERROR", e.message!!)
        }
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int, position: Int) {
        if(viewHolder is PostAdapter.PostHolder){
            this.postAdapter!!.removeItem(viewHolder.adapterPosition)
        }
    }

    override fun onClick(v: View?) {
        when(v){
            this.binding.btnRefresh->{
                getPosts()
                isDeleted= false;
            }
            this.binding.btnDelete->{
                deleteAllPosts()
                supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
            this.binding.btnFilter->{
                loadFragment(FilterFavoriteFragment())
            }
        }
    }
}