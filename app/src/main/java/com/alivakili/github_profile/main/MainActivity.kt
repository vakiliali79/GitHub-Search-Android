package com.alivakili.github_profile.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import com.alivakili.github_profile.databinding.ActivityMainBinding
import com.alivakili.github_profile.databinding.LayoutChipBinding
import com.alivakili.github_profile.profile.ProfileActivity
import com.alivakili.github_profile.repository.RepositoryActivity
import com.google.android.material.chip.Chip

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showProgressBar()
        properties()
        checkEditText()
        hideProgressBar()
    }

    private fun checkEditText(){

        binding.searchEditText.setOnKeyListener(View.OnKeyListener { view, i, keyEvent ->
            if (i==KeyEvent.KEYCODE_ENTER&&keyEvent.action==KeyEvent.ACTION_UP){
                val searchWord=binding.searchEditText.text.toString()
                var intent:Intent

                val message: String = if (binding.switch1.isChecked) "language" else "user"
                if(message=="user")
                    intent=Intent(this@MainActivity,ProfileActivity::class.java)
                else
                    intent=Intent(this@MainActivity,RepositoryActivity::class.java)
                intent.putExtra("KEY_SEARCH_WORD",searchWord)
                intent.putExtra("KEY_SEARCH_STATE",message)
                startActivity(intent)
            }
            return@OnKeyListener true })
    }

    private fun properties(){
        val languages: List<String> = listOf("Kotlin","Python","Java","C++")
        val organizations: List<String> = listOf("JetBrains","google","github")
        val users: List<String> =listOf("hlinero","torvalds","gaearon")
        showPopularLanguages(languages)
        showPopularOrganizations(organizations)
        showPopularUsers(users)
    }

    private fun showPopularLanguages(languages: List<String>){
        for (language in languages){
            binding.langChipGroup.addView(createChip(language))
        }
    }
    private fun showPopularOrganizations(organizations: List<String>){
        for (organization in organizations){
            binding.organizationChipGroup.addView(createChip(organization))
        }
    }
    private fun showPopularUsers(users: List<String>){
        for (user in users){
            binding.userChipGroup.addView(createChip(user))
        }
    }

    private fun showErrorMessage(){
        Toast.makeText(this,"Unable to retrieve data!", Toast.LENGTH_LONG).show()
    }
    private fun showProgressBar(){
        binding.progressBar.visibility= View.VISIBLE
    }

    private fun hideProgressBar(){
        binding.progressBar.visibility= View.GONE
    }
    private fun createChip(label:String): Chip {
        val chip = LayoutChipBinding.inflate(layoutInflater).root
        chip.text=label
        chip.setOnClickListener{
            binding.searchEditText.setText(label)
        }
        return chip
    }
}