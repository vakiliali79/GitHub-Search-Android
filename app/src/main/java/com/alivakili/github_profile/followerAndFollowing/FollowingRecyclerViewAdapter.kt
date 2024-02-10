package com.alivakili.github_profile.followerAndFollowing

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alivakili.github_profile.R
import com.alivakili.github_profile.databinding.LayoutFollowersAndFollowingBinding
import com.squareup.picasso.Picasso

class FollowingRecyclerViewAdapter(
    private val context: Context,
    private val items:FollowingDTO?,
    private val onClicked:(FollowingDTO.Following?)->Unit,
): RecyclerView.Adapter<FollowingRecyclerViewAdapter.FollowingViewModel>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            FollowingViewModel {
        return FollowingViewModel.create(parent, onClicked)
    }



    override fun onBindViewHolder(holder: FollowingViewModel, position: Int) {
        val profile= items?.get(position)
        holder.bind(profile,context)
    }



    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    class FollowingViewModel(
        private val binding: LayoutFollowersAndFollowingBinding,
        private val onClicked: (FollowingDTO.Following?) -> Unit
    ): RecyclerView.ViewHolder(binding.root)  {

        companion object{
            fun create(parent: ViewGroup, onClicked:(FollowingDTO.Following?)->Unit): FollowingViewModel {
                val binding= LayoutFollowersAndFollowingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false)
                return FollowingViewModel(
                    binding = binding,
                    onClicked = onClicked
                )
            }
        }

        fun bind(following: FollowingDTO.Following?, context: Context){
            binding.profileIV
            loadImage(following?.avatarUrl, context = context)
            binding.root.setOnClickListener(View.OnClickListener {
                onClicked(following)
            })
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