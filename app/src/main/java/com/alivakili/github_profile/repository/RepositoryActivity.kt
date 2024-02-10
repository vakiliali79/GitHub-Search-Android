package com.alivakili.github_profile.repository

import android.content.Intent
import android.net.Uri
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
import com.alivakili.github_profile.databinding.ActivityRepositoryBinding

import kotlinx.coroutines.launch

class RepositoryActivity : AppCompatActivity() {
    var searchState: String = " "

    private lateinit var binding: ActivityRepositoryBinding
    private var userName:String?=""
    private val viewModel: RepositoryViewModel by viewModels{
        RepositoryViewModel.factory(userName, stateWord = searchState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRepositoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userName=searchWord()
        searchState = getState()

        configureToolbar(title=userName)
        observeState()



    }


    private fun searchWord(): String {
        return intent.getStringExtra("KEY_SEARCH_WORD")!!
    }

    private fun getState(): String {
        return intent.getStringExtra("KEY_SEARCH_STATE")!!
    }

    private fun configureToolbar(title:String?){
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
                        RepositoryState.Loading -> showProgressBar()
                        is RepositoryState.Success -> {
                            showRepository(state.repository)
                        }
                        is RepositoryState.SuccessOnLanguage->{
                            showLanguageRepo(state.repository)
                        }

                        RepositoryState.Failure -> showErrorMessage()
                    }
                }
            }
        }
    }
    private fun showLanguageRepo(repository: RepositoryDataCap?){
        hideProgressBar()

        binding.recyclerView.apply {
            addItemDecoration(DividerItemDecoration(this@RepositoryActivity, LinearLayout.VERTICAL))
            adapter= RepositoryRecyclerViewAdapter(items = repository, onClicked =::openRepositoryWebPage,
                context = this@RepositoryActivity
            )
            layoutManager= LinearLayoutManager(this@RepositoryActivity)
            setHasFixedSize(true)
            visibility= View.VISIBLE
        }
    }


    private fun showRepository(repository: RepositoryDataCap?){
        hideProgressBar()

        binding.recyclerView.apply {
            addItemDecoration(DividerItemDecoration(this@RepositoryActivity, LinearLayout.VERTICAL))
            adapter= RepositoryRecyclerViewAdapter(items = repository, onClicked =::openRepositoryWebPage,
                context = this@RepositoryActivity
            )
            layoutManager= LinearLayoutManager(this@RepositoryActivity)
            setHasFixedSize(true)
            visibility= View.VISIBLE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun showErrorMessage(){
        Toast.makeText(this,"Unable to retrieve followers!", Toast.LENGTH_LONG).show()
    }
    private fun showProgressBar(){
        binding.progressBar.visibility= View.VISIBLE
    }

    private fun openRepositoryWebPage(repo: RepositoryDataCap.RepositoryItem?) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(repo?.url))
        startActivity(intent)
    }

    private fun hideProgressBar(){
        binding.progressBar.visibility= View.GONE
    }

}