package com.alivakili.github_profile.repository

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alivakili.github_profile.databinding.LayoutRepositoryBinding

class RepositoryRecyclerViewAdapter (
    private val context: Context,
    private val items: RepositoryDataCap?,
    private val onClicked:(RepositoryDataCap.RepositoryItem?)->Unit,
): RecyclerView.Adapter<RepositoryRecyclerViewAdapter.RepositoryViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            RepositoryViewHolder {
        return RepositoryViewHolder.create(parent, onClicked)
    }



    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        val repo= items?.get(position)
        holder.bind(repo,context)
    }



    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    class RepositoryViewHolder(
        private val binding: LayoutRepositoryBinding,
        private val onClicked: (RepositoryDataCap.RepositoryItem?) -> Unit
    ): RecyclerView.ViewHolder(binding.root)  {

        companion object{
            fun create(parent: ViewGroup, onClicked:(RepositoryDataCap.RepositoryItem?)->Unit): RepositoryViewHolder {
                val binding= LayoutRepositoryBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false)
                return RepositoryViewHolder(
                    binding = binding,
                    onClicked = onClicked
                )
            }

        }

        fun bind(repository: RepositoryDataCap.RepositoryItem?, context: Context){
            binding.dateTV.text=repository?.updatedAt?.split("T")?.first()?:""
            binding.repNameTV.text=repository?.name
            binding.languageRepTV.text=repository?.language
            binding.descriptionTV.text=repository?.description
            binding.rightTV.text=repository?.login
            binding.root.setOnClickListener{
                onClicked(repository)
            }
        }




    }



}