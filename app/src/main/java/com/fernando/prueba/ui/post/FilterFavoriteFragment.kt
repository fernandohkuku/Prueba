package com.fernando.prueba.ui.post

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.fernando.prueba.R
import com.fernando.prueba.databinding.FragmentFilterFavoriteBinding
import com.fernando.prueba.interfaces.PostListener
import com.fernando.prueba.models.Post
import com.fernando.prueba.ui.post.adapters.PostAdapter
import com.fernando.prueba.utils.Coroutines
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class FilterFavoriteFragment : Fragment(R.layout.fragment_filter_favorite), KodeinAware , PostListener, View.OnClickListener{

    override val kodein by kodein()

    private val binding:FragmentFilterFavoriteBinding by viewBinding()

    private val postViewModelFactory:PostViewModelFactory by instance()

    private lateinit var postViewModel: PostViewModel

    private var allPost = listOf<Post>()

    private var postFavorites = listOf<Post>()


    private var postAdapter:PostAdapter?=null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(this.binding.toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true);
        activity.supportActionBar?.setDisplayShowTitleEnabled(false);
        this.binding.toolbar.setNavigationIcon(R.drawable.ic_back);


        postViewModel = ViewModelProvider(this, postViewModelFactory).get(PostViewModel::class.java);

        postViewModel.getAllPosts().observe(viewLifecycleOwner, {posts->
            initRecycler(posts)
            allPost = posts
        })

        postViewModel.getPostFavorites().observe(viewLifecycleOwner, {posts->
            postFavorites = posts
        })

        this.binding.toolbar.setNavigationOnClickListener {
            activity.supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }


        this.binding.btnAll.setOnClickListener(this)
        this.binding.btnFavorites.setOnClickListener(this)

    }


    private fun initRecycler(posts:List<Post>){
        postAdapter = PostAdapter(requireContext());
        this.binding.rcvFilter.setHasFixedSize(true);
        this.binding.rcvFilter.layoutManager = LinearLayoutManager(requireContext());
        this.postAdapter?.setData(posts, this)
        this.binding.rcvFilter.adapter = postAdapter

    }

    override fun onSelectedPost(post: Post, position: Int) {

    }

    override fun onDeletePost(post: Post, position: Int) = Coroutines.main {

    }

    override fun onClick(v: View?) {
        when(v){
            this.binding.btnAll->{
                initRecycler(allPost)
            }
            this.binding.btnFavorites->{
                initRecycler(postFavorites)
            }
        }
    }
}