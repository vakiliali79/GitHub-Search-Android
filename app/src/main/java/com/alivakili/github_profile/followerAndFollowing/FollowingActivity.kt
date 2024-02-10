package com.alivakili.github_profile.followerAndFollowing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.alivakili.github_profile.R
import com.alivakili.github_profile.databinding.ActivityFollowingBinding
import com.alivakili.github_profile.profile.ProfileActivity
import kotlinx.coroutines.launch

class FollowingActivity : AppCompatActivity() {
    private lateinit var binding:ActivityFollowingBinding
    private var userName:String?=""
    private val viewModel: FollowingViewModel by viewModels{
        FollowingViewModel.factory(userName)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityFollowingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userName=intent.getStringExtra("KEY_SEARCH_WORD")
        configureToolbar(title="Following")
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
                        FollowingState.Loading -> showProgressBar()
                        is FollowingState.Success -> {
                            showNews(state.following)
                        }

                        FollowingState.Failure -> showErrorMessage()
                    }
                }
            }
        }
    }



    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }


    private fun showNews(following:FollowingDTO?){
        hideProgressBar()

        binding.recyclerView.apply {
            addItemDecoration(DividerItemDecoration(this@FollowingActivity, LinearLayout.VERTICAL))
            adapter= FollowingRecyclerViewAdapter(items = following, onClicked =::showFollowingProfile,
                context = this@FollowingActivity
            )
            layoutManager= LinearLayoutManager(this@FollowingActivity)
            setHasFixedSize(true)
            visibility= View.VISIBLE
        }
    }
    private fun showErrorMessage(){
        Toast.makeText(this,"Unable to retrieve followings!", Toast.LENGTH_LONG).show()
    }
    private fun showProgressBar(){
        binding.progressBar.visibility= View.VISIBLE
    }

    private fun hideProgressBar(){
        binding.progressBar.visibility= View.GONE
    }
    private fun showFollowingProfile(userName:FollowingDTO.Following?){

        var intent= Intent(this@FollowingActivity,ProfileActivity::class.java)
        intent.putExtra("KEY_SEARCH_WORD",userName?.login)
        intent.putExtra("KEY_SEARCH_STATE","user")
        startActivity(intent)
    }
}