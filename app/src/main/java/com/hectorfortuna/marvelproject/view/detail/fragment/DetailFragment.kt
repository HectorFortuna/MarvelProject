package com.hectorfortuna.marvelproject.view.detail.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.bumptech.glide.Glide
import com.hectorfortuna.marvelproject.R
import com.hectorfortuna.marvelproject.core.Status
import com.hectorfortuna.marvelproject.data.model.Favorites
import com.hectorfortuna.marvelproject.data.model.User
import com.hectorfortuna.marvelproject.data.model.comics.Date
import com.hectorfortuna.marvelproject.data.model.comics.Price
import com.hectorfortuna.marvelproject.data.model.comics.Result
import com.hectorfortuna.marvelproject.databinding.CharacterDetailBinding
import com.hectorfortuna.marvelproject.util.apiKey
import com.hectorfortuna.marvelproject.util.hash
import com.hectorfortuna.marvelproject.util.ts
import com.hectorfortuna.marvelproject.view.detail.adapter.CarouselAdapter
import com.hectorfortuna.marvelproject.view.detail.decoration.BoundsOffsetDecoration
import com.hectorfortuna.marvelproject.view.detail.decoration.LinearHorizontalSpacingDecoration
import com.hectorfortuna.marvelproject.view.detail.decoration.ProminentLayoutManager
import com.hectorfortuna.marvelproject.view.detail.viewmodel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
@AndroidEntryPoint
class DetailFragment : Fragment() {
    private val viewModel by viewModels<DetailViewModel>()

    private lateinit var snapHelper: SnapHelper
    private lateinit var carouselAdapter: CarouselAdapter
    private lateinit var binding: CharacterDetailBinding
    private lateinit var favorite: Favorites
    private lateinit var user: User
    private var checkCharacter: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CharacterDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favorite = arguments?.getParcelable<Favorites>("FAVORITE") as Favorites

        getUserByIntent()

        viewModel.verifySavedCharacter(favorite.id, user.email)
        getCategory(id = favorite.id)
        getSeries(id = favorite.id)

        binding.run {
            setCharacterScreen()
            setFavoriteCharacter()
        }

        observeVMEvents()
    }

    private fun CharacterDetailBinding.setCharacterScreen() {
        setImage(imgDetail)
        detailsTitle.text = favorite.name
        detailsDescription.text = favorite.description
    }

    private fun getUserByIntent() {
        activity?.let {
            user = it.intent.getParcelableExtra<User>("USER") as User
        }
    }

    private fun CharacterDetailBinding.setFavoriteCharacter() {
        fabDetails.setOnClickListener{
            if (checkCharacter) {
                val newFavorite = favorite.copy(email = user.email)
                viewModel.deleteCharacter(newFavorite)
                fabDetails.setIconResource(R.drawable.ic_fab)
                fabDetails.setText(getString(R.string.fab_favoritar))
                checkCharacter = false
            } else {
                val copyFavorite = favorite.copy(email = user.email)
                viewModel.insertFavorite(copyFavorite)
                fabDetails.setIconResource(R.drawable.ic_full_favourite)
                fabDetails.setText(getString(R.string.fab_favoritado))
                checkCharacter = true
            }
        }
    }

    private fun getCategory(id: Long){
        val ts = ts()
        viewModel.getCategory(apiKey(), hash(ts), ts.toLong(), id)
    }

    private fun getSeries(id: Long){
        val ts = ts()
        viewModel.getSeries(apiKey(), hash(ts), ts.toLong(), id)
    }

    private fun observeVMEvents() {
        viewModel.response.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { response ->
                        Timber.tag("Success").i(response.toString())
                    }
                }
                Status.ERROR -> {
                    Timber.tag("Error").i(it.error)
                }
                Status.LOADING -> {}
            }
        }
        viewModel.comicsResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { category ->
                        setRecycler(category.data.results)
                    }
                }
                Status.ERROR -> {
                    Timber.tag("Error").i(it.error)
                }
                Status.LOADING -> {}
            }
        }
        viewModel.seriesResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { category ->
                        setRecyclerSeries(category.data.results)
                    }
                }
                Status.ERROR -> {
                    Timber.tag("Error").i(it.error)
                }
                Status.LOADING -> {}
            }
        }
        viewModel.verifyCharacter.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { exist ->
                        if (exist) {
                            binding.fabDetails.setIconResource(R.drawable.ic_full_favourite)
                            binding.fabDetails.setText(R.string.fab_favoritado)
                        }
                        checkCharacter = exist
                    }
                }
                Status.ERROR -> {
                    Timber.tag("Error").i(it.error)
                }
                Status.LOADING -> {}
            }
        }
    }

    private fun setAdapter(list: List<Result>) {
        carouselAdapter = CarouselAdapter(list){
            binding.run {
                setupComicsImage(it)
                setupComicsScreen(it)
            }
        }
    }

    private fun CharacterDetailBinding.setupComicsImage(it: Result) {
        Glide.with(this@DetailFragment)
            .load("${it.thumbnail.path}.${it.thumbnail.extension}")
            .into(imgDetail)
    }

    private fun CharacterDetailBinding.setupComicsScreen(it: Result) {
        detailsTitle.text = it.title
        detailsDescription.text = it.description

        fabDetails.visibility = View.INVISIBLE
        backToCharacter()

        val page = it.pageCount.toString()
        val price = it.prices
        val itemPrice = it.prices?.first()?.price
        setPrices(price, itemPrice)
        setPage(page, it)
        setDate(it, it.dates)
    }

    private fun CharacterDetailBinding.backToCharacter() {
        fabBackToCharacter.visibility = View.VISIBLE
        fabBackToCharacter.setOnClickListener {
            setCharacterScreen()
            fabBackToCharacter.visibility = View.GONE
            comicsPages.visibility = View.INVISIBLE
            comicsPrice.visibility = View.INVISIBLE
            comicsOnSaleDate.visibility = View.INVISIBLE
            fabDetails.visibility = View.VISIBLE
        }
    }

    private fun CharacterDetailBinding.setDate(
        it: Result,
        dates: List<Date?>
    ) {
        if (it.dates.isNullOrEmpty()) {
            comicsOnSaleDate.visibility = View.INVISIBLE
        } else {
            comicsOnSaleDate.visibility = View.VISIBLE
            comicsOnSaleDate.text = "Release Date: ${dates.first()?.date?.substring(0, 10)?.replace("-", "/")}"
        }
    }

    private fun CharacterDetailBinding.setPage(
        page: String,
        it: Result
    ) {
        if (page.isNullOrBlank() || page == "0") {
            comicsPages.visibility = View.INVISIBLE
        } else {
            comicsPages.visibility = View.VISIBLE
            comicsPages.text = "Pages: ${it.pageCount}"
        }
    }

    private fun CharacterDetailBinding.setPrices(
        price: List<Price>?,
        itemPrice: Double?,

    ) {
        if (price.isNullOrEmpty() || itemPrice.toString()  == "0.0") {
            comicsPrice.visibility = View.INVISIBLE
        } else {
            comicsPrice.visibility = View.VISIBLE
            comicsPrice.text = "Price: $${price.first()?.price.toString()}"
        }
    }

    private fun setRecycler(list: List<Result>) {
        setAdapter(list)
        snapHelper = PagerSnapHelper()

        binding.run {
            recyclerCategory.apply {
                adapter = carouselAdapter
                layoutManager = ProminentLayoutManager(context)
                setItemViewCacheSize(4)
                val spacing = resources.getDimensionPixelSize(R.dimen.carousel_spacing)
                addItemDecoration(LinearHorizontalSpacingDecoration(spacing))
                addItemDecoration(BoundsOffsetDecoration())
            }
        }

        snapHelper.attachToRecyclerView(binding.recyclerCategory)
    }

    private fun setRecyclerSeries(list: List<Result>){
        setAdapter(list)
        snapHelper = PagerSnapHelper()

        binding.run {
            recyclerCategorySeries.apply {
                adapter = carouselAdapter
                layoutManager = ProminentLayoutManager(context)
                setItemViewCacheSize(4)
                val spacing = resources.getDimensionPixelSize(R.dimen.carousel_spacing)
                addItemDecoration(LinearHorizontalSpacingDecoration(spacing))
                addItemDecoration(BoundsOffsetDecoration())
            }
        }

        snapHelper.attachToRecyclerView(binding.recyclerCategorySeries)
    }

    private fun setImage(image: AppCompatImageView) {
        Glide.with(this@DetailFragment)
            .load("${favorite.thumbnail.path}.${favorite.thumbnail.extension}")
            .centerCrop()
            .into(image)
    }
}