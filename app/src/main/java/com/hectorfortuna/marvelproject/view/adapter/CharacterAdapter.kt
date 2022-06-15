package com.hectorfortuna.marvelproject.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hectorfortuna.marvelproject.data.model.Results
import com.hectorfortuna.marvelproject.databinding.CharacterItemBinding

class CharacterAdapter(
    characterList: MutableList<Results>
) :
    RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {
    private val characterMutable: MutableList<Results> = characterList


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding =
            CharacterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharacterViewHolder(binding)
    }

    override fun getItemCount() = characterMutable.count()


    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bindView(characterMutable[position])

    }

    class CharacterViewHolder(
        private val binding: CharacterItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(character: Results) {
            binding.run {
                txtNameCharacterItem.text = character.name
                val portrait = "/portrait_medium."
                Glide.with(itemView)
                    .asBitmap()
                    .load("${character.thumbnail.path}${portrait}${character.thumbnail.extension}")
                    .centerCrop()
                    .into(imgItem)


            }
        }

    }
}