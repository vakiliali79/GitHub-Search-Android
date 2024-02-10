package com.alivakili.github_profile.followerAndFollowing

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alivakili.github_profile.R
import com.alivakili.github_profile.databinding.LayoutFollowersAndFollowingBinding
import com.squareup.picasso.Picasso

class FollowersRecyclerViewAdapter(
    private val context:Context,
    private val items:FollowersDTO?,
    private val onClicked:(FollowersDTO.Followers?)->Unit,
): RecyclerView.Adapter<FollowersRecyclerViewAdapter.FollowersViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            FollowersViewHolder {
        return FollowersViewHolder.create(parent, onClicked)
    }



    override fun onBindViewHolder(holder: FollowersViewHolder, position: Int) {
        val profile= items?.get(position)
        holder.bind(profile,context)
    }



    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    class FollowersViewHolder(
        private val binding: LayoutFollowersAndFollowingBinding,
        private val onClicked: (FollowersDTO.Followers?) -> Unit
    ): RecyclerView.ViewHolder(binding.root)  {

        companion object{
            fun create(parent: ViewGroup, onClicked:(FollowersDTO.Followers?)->Unit): FollowersViewHolder {
                val binding= LayoutFollowersAndFollowingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false)
                return FollowersViewHolder(
                    binding = binding,
                    onClicked = onClicked
                )
            }
        }

        fun bind(followers: FollowersDTO.Followers?,context: Context){
            binding.userNameTV.text=followers?.login
            loadImage(followers?.avatarUrl, context = context)

            binding.root.setOnClickListener{
                onClicked(followers)
            }
        }

        private fun loadImage(url:String?,context: Context) {
            Log.e("TAG", "loadImage: "+url )
            Picasso.with(context).load(url).fit().centerCrop()
                .placeholder(R.drawable.github_logo)
                .error(R.drawable.github_logo)
                .into(binding.profileIV);

        }


    }



}