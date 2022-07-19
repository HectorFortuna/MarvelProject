package com.hectorfortuna.marvelproject.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hectorfortuna.marvelproject.data.model.Results
import com.hectorfortuna.marvelproject.databinding.CharacterItemBinding

class CharacterAdapter(
    private val characterList: List<Results>,
    private val itemClick: ((item: Results) -> Unit),
    private val longClick: ((item:Results) -> Unit)? = null
) :
    RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding =
            CharacterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharacterViewHolder(binding, itemClick, longClick)
    }

    override fun getItemCount() = characterList.count()

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bindView(characterList[position])

    }

    class CharacterViewHolder(
        private val binding: CharacterItemBinding,
        private val itemClick: (item: Results) -> Unit,
        private val longClick: ((item: Results) -> Unit)?
    )
        : RecyclerView.ViewHolder(binding.root) {
        fun bindView(character: Results) {
            binding.run {
                txtNameCharacterItem.text = character.name

                Glide.with(itemView)
                    .load("${character.thumbnail.path}.${character.thumbnail.extension}")
                    .centerCrop()
                    .into(imgItem)


                itemView.setOnClickListener {
                    itemClick.invoke(character)
                }

                itemView.setOnLongClickListener{
                    longClick?.invoke(character)
                    return@setOnLongClickListener true
                }
            }
        }
    }
}


