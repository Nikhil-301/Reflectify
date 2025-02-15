package com.example.reflectify

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.reflectify.databinding.JournalRowBinding

class JournalRecyclerAdapter(val context: Context,var journalList:List<Journal>)
    : RecyclerView.Adapter<JournalRecyclerAdapter.MyViewHolder>() {

    lateinit var binding: JournalRowBinding



    class MyViewHolder(var binding: JournalRowBinding) :
        RecyclerView.ViewHolder(binding.root){
            fun bind(journal: Journal){
                binding.journal = journal

            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        binding = JournalRowBinding.inflate(
            LayoutInflater.from(context),
            parent,false)

        return MyViewHolder(binding)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val journal = journalList[position]
        holder.bind(journal)

    }

    override fun getItemCount(): Int = journalList.size

}



