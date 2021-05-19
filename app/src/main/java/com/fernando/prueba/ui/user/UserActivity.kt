package com.fernando.prueba.ui.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.fernando.prueba.R
import com.fernando.prueba.databinding.ActivityUserBinding
import com.fernando.prueba.interfaces.ResponseListener
import com.fernando.prueba.models.Post
import com.fernando.prueba.models.User
import com.fernando.prueba.utils.ApiServiceException
import com.fernando.prueba.utils.Coroutines
import com.fernando.prueba.utils.NoInternetException
import kotlinx.android.synthetic.main.activity_user.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.lang.Exception

class UserActivity : AppCompatActivity(R.layout.activity_user), KodeinAware, ResponseListener, View.OnClickListener{

    override val kodein by kodein()

    private val userViewModelFactory:UserViewModelFactory by instance()

    private lateinit var userViewModel: UserViewModel

    private val binding:ActivityUserBinding by viewBinding()

    private var post:Post ?=null;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onStarted()

        this.userViewModel = ViewModelProvider(this, userViewModelFactory).get(UserViewModel::class.java)

        intent.extras.also {bundle ->
            if (bundle!=null){
                post = bundle.getSerializable("post") as Post
                isFavoriteChecked(post)
                getUser(post!!.userId!!)
            }
        }

        this.binding.tvFavorite.setOnClickListener(this)
        this.binding.tvFavoriteMessage.setOnClickListener(this)


    }

    private fun isFavoriteChecked(post: Post?){
        if (post?.isFavorite!!){
            this.binding.tvFavoriteMessage.text = "Favorite post";
            this.binding.tvFavorite.setBackgroundResource(R.drawable.ic_favorite_checked)
        }else{
            this.binding.tvFavoriteMessage.text = "Add to favorite posts";
            this.binding.tvFavorite.setBackgroundResource(R.drawable.ic_favorite_unchecked)
        }
    }


    private fun getUser(userId:Int) = Coroutines.main {
        try {
            userViewModel.getUser(userId.toString()).also {user ->
                if(user!=null){
                    bindUI(user)
                }
            }
        }catch (e:ApiServiceException){
            onFailure(e.message!!)
        }catch (e:NoInternetException){
            onFailure(e.message!!)
        }catch (e:Exception){
            onFailure(e.message!!)
        }
    }

    private fun setFavoritePost() = Coroutines.main{
        if (post!!.isFavorite!!){
            this.userViewModel.setFavorite(false, post!!.id!!)
            bindFavoritePost(R.drawable.ic_favorite_unchecked, "The post was removed from favorite", "Add to favorite posts")

        }else{
            this.userViewModel.setFavorite(true, post!!.id!!)
            bindFavoritePost(R.drawable.ic_favorite_checked, "The post was added to favorite", "Favorite post")
        }
        post!!.isFavorite = !post!!.isFavorite!!
    }

    private fun bindFavoritePost(icon:Int, message: String, label:String){
        this.binding.tvFavorite.setBackgroundResource(icon)
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        this.binding.tvFavoriteMessage.text = label
    }

    private fun bindUI(user:User){
        this.binding.tvName.text =  user.name.toString();
        this.binding.tvUsername.text = "Username: ${user.username.toString()}";
        this.binding.tvEmail.text = "Email: ${user.email}"
        this.binding.tvPhone.text = "Phone: ${user.phone.toString().split(" ")[0]}"
        this.binding.tvWebSite.text = "Site: ${user.website.toString()}"
        this.binding.tvStreet.text = "Street: ${user.address?.street.toString()}";
        this.binding.tvSuite.text = "Suite: ${user.address?.suite.toString()}";
        this.binding.tvCity.text = "City: ${user.address?.city.toString()}";
        this.binding.tvZipCode.text = "Zip Code: ${user.address?.zipcode.toString()}";
        this.binding.tvNameCompany.text = user.company?.name.toString();
        this.binding.tvCatchPhrase.text = user.company?.catchPhrase.toString();
        this.binding.tvBs.text = user.company?.bs.toString();
        onSuccess()
    }

    override fun onStarted() {

        this.binding.progressBar.visibility = View.VISIBLE
    }

    override fun onFailure(message: String)= Coroutines.main{
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSuccess() {
        this.binding.progressBar.visibility = View.GONE
        this.binding.containerUser.visibility = View.VISIBLE
    }

    override fun onClick(v: View?) {
        when(v){
            this.binding.tvFavorite->{
                setFavoritePost()
            }
            this.binding.tvFavoriteMessage->{
                setFavoritePost()
            }
        }
    }
}