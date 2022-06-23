package com.hectorfortuna.marvelproject.view.home.fragment.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.hectorfortuna.marvelproject.R
import com.hectorfortuna.marvelproject.data.model.Results
import com.hectorfortuna.marvelproject.databinding.CharacterDetailBinding

class DetailFragment : Fragment() {
    private lateinit var binding: CharacterDetailBinding
    private lateinit var character: Results

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = CharacterDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        character = arguments?.getSerializable("CHARACTER") as Results

        binding.run {
            setImage(imgDetails)
            setImage(imgDetailsPrincipal)

            txtDetails.text = character.name
            txtDescription.text = character.description
            fabDetails.setOnClickListener{
                binding.fabDetails.setImageResource(R.drawable.ic_full_favourite)
            }
        }
    }

    private fun setImage(image: AppCompatImageView) {
        Glide.with(this@DetailFragment)
            .load("${character.thumbnail.path}.${character.thumbnail.extension}")
            .centerCrop()
            .into(image)
    }


}