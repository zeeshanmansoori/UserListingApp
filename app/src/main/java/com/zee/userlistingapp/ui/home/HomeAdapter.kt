package com.zee.userlistingapp.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.zee.userlistingapp.databinding.SingleUserLayoutBinding
import com.zee.userlistingapp.model.User

class HomeAdapter(val userItemClickListener: UserItemClickListener):RecyclerView.Adapter<HomeAdapter.Holder>(){

    private val list = mutableListOf<User>()

    fun submitList(ls:List<User>){
        list.clear()
        list.addAll(ls)
        notifyDataSetChanged()
    }
    inner class Holder(private val binding: SingleUserLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {

            binding.cardView.setOnClickListener {

                if (adapterPosition != RecyclerView.NO_POSITION)
                    userItemClickListener.onUserItemClicked(list[adapterPosition])
            }
        }
        fun bind(user: User){
            binding.apply {
                name.text = user.login
                Picasso.get().load(user.avatar_url).into(userProfile)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            SingleUserLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        if (RecyclerView.NO_POSITION != position)
            holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size


    interface UserItemClickListener{
        fun onUserItemClicked(user: User)
    }
}