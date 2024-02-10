package com.alivakili.github_profile.followerAndFollowing

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import com.alivakili.github_profile.R
import com.alivakili.github_profile.databinding.ActivityFollowersBinding
import com.alivakili.github_profile.profile.ProfileActivity
import kotlinx.coroutines.launch

class FollowersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFollowersBinding
    private var userName:String?=""
    private val viewModel: FollowersViewModel by viewModels{
        FollowersViewModel.factory(userName)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityFollowersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userName=intent.getStringExtra("KEY_SEARCH_WORD")

        configureToolbar(title="Followers")
        observeState()

    }

    private fun configureToolbar(title:String){
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            this.title=title
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)
        }
    }

    private fun observeState(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel.state.collect{
                        state->
                    when(state){
                        FollowersState.Loading -> showProgressBar()
                        is FollowersState.Success -> {
                            showProfile(state.followers)
                        }

                        FollowersState.Failure -> showErrorMessage()
                    }
                }
            }
        }
    }



    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }


    private fun showProfile(followers:FollowersDTO?){
        hideProgressBar()

        binding.recyclerView.apply {
            addItemDecoration(DividerItemDecoration(this@FollowersActivity, LinearLayout.VERTICAL))
            adapter= FollowersRecyclerViewAdapter(items = followers, onClicked =::showFollowersProfile,
                context = this@FollowersActivity
            )
            layoutManager= LinearLayoutManager(this@FollowersActivity)
            setHasFixedSize(true)
            visibility= View.VISIBLE
        }
    }
    private fun showErrorMessage(){
        Toast.makeText(this,"Unable to retrieve followers!", Toast.LENGTH_LONG).show()
    }
    private fun showProgressBar(){
        binding.progressBar.visibility= View.VISIBLE
    }

    private fun hideProgressBar(){
        binding.progressBar.visibility= View.GONE
    }
    private fun showFollowersProfile(userName:FollowersDTO.Followers?){

        var intent= Intent(this@FollowersActivity, ProfileActivity::class.java)
        intent.putExtra("KEY_SEARCH_WORD",userName?.login)
        intent.putExtra("KEY_SEARCH_STATE","user")
        startActivity(intent)
    }
}