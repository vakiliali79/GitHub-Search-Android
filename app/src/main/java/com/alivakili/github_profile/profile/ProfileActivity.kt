package com.alivakili.github_profile.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.alivakili.github_profile.R
import com.alivakili.github_profile.databinding.ActivityProfileBinding

import com.alivakili.github_profile.followerAndFollowing.FollowersActivity
import com.alivakili.github_profile.followerAndFollowing.FollowingActivity
import com.alivakili.github_profile.repository.RepositoryActivity
import com.alivakili.github_profile.userDTO.User
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch


class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val viewModel: ProfileViewModel by viewModels {
        ProfileViewModel.factory(searchWord)
    }
    var searchWord: String = " "
    var searchState: String = " "


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)

        setContentView(binding.root)
        searchWord = searchWord()
        searchState = getState()
        configureToolbar(title = searchWord)
        getProfile(q = searchWord, searchState)

    }

    private fun getProfile(q: String, searchState: String) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.state.collect { state ->
                    when (state) {
                        ProfileState.Loading -> showProgressBar()
                        is ProfileState.SuccessOnUser -> {
                            hideProgressBar()
                            showProfile(state.profileDOT)
                        }
                        ProfileState.Failure -> showErrorMessage()
                    }
                }
            }
        }

    }



    private fun showProfile(profileDTO: User?) {
        binding.bioTV.text = profileDTO?.bio
        binding.webSiteTV.setOnClickListener {
            openBrowser(profileDTO?.blog)
        }
        binding.webSiteTV.text=profileDTO?.blog
        binding.companyTV.text=profileDTO?.company
        binding.twitterTV.text=profileDTO?.twitterUsername
        loadImage(profileDTO?.avatarUrl.toString())
        binding.followerTV.text = profileDTO?.followers.toString()
        binding.followingTV.text = profileDTO?.following.toString()
        binding.repositoriesTV.text = profileDTO?.publicRepos.toString()
        binding.userNameTV.text = profileDTO?.name
        binding.locationTV.text = profileDTO?.location
        if (profileDTO?.hireable==null)
            binding.hireableTV.text="Not hireable"
        else
            binding.hireableTV.text="Hireable"
        binding.followerTV.setOnClickListener(View.OnClickListener {

            val intent=Intent(this@ProfileActivity,FollowersActivity::class.java)
            intent.putExtra("KEY_SEARCH_WORD",searchWord)
            startActivity(intent)
        })
        binding.followingTV.setOnClickListener(View.OnClickListener {
            var intent=Intent(this@ProfileActivity, FollowingActivity::class.java)
            intent.putExtra("KEY_SEARCH_WORD",searchWord)
            startActivity(intent)
        })
        binding.repositoriesTV.setOnClickListener(View.OnClickListener {
            var intent=Intent(this@ProfileActivity, RepositoryActivity::class.java)
            intent.putExtra("KEY_SEARCH_STATE","user")
            intent.putExtra("KEY_SEARCH_WORD",searchWord)
            startActivity(intent)
        })
        binding.twitterTV.setOnClickListener{
        openBrowser("https://twitter.com/"+profileDTO?.twitterUsername)
    }

    }

    private fun openBrowser(url: String?) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun loadImage(url: String?) {
        val imageView=findViewById<ImageView>(R.id.profileIV)
        Log.e("TAG", "loadImage: "+url )
        Picasso.with(this@ProfileActivity).load(url).fit().centerCrop()
            .placeholder(R.drawable.github_logo)
            .error(R.drawable.github_logo)
            .into(imageView);
    }

    private fun configureToolbar(title: String) {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            this.title = title
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun showErrorMessage() {
        Toast.makeText(this, "Unable to retrieve data!", Toast.LENGTH_LONG).show()
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    private fun searchWord(): String {
        return intent.getStringExtra("KEY_SEARCH_WORD")!!
    }

    private fun getState(): String {
        return intent.getStringExtra("KEY_SEARCH_STATE")!!
    }


}