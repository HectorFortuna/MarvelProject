package com.hectorfortuna.marvelproject.view.detail.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.bumptech.glide.Glide
import com.hectorfortuna.marvelproject.R
import com.hectorfortuna.marvelproject.core.Status
import com.hectorfortuna.marvelproject.data.db.AppDatabase
import com.hectorfortuna.marvelproject.data.db.CharacterDAO
import com.hectorfortuna.marvelproject.data.db.repository.DatabaseRepository
import com.hectorfortuna.marvelproject.data.db.repository.DatabaseRepositoryImpl
import com.hectorfortuna.marvelproject.data.model.Comics
import com.hectorfortuna.marvelproject.data.model.Favorites
import com.hectorfortuna.marvelproject.data.model.User
import com.hectorfortuna.marvelproject.data.model.comics.Result
import com.hectorfortuna.marvelproject.data.network.ApiService
import com.hectorfortuna.marvelproject.data.repository.comics.CategoryRepository
import com.hectorfortuna.marvelproject.data.repository.comics.CategoryRepositoryImpl
import com.hectorfortuna.marvelproject.databinding.CharacterDetailBinding
import com.hectorfortuna.marvelproject.util.apiKey
import com.hectorfortuna.marvelproject.util.hash
import com.hectorfortuna.marvelproject.util.ts
import com.hectorfortuna.marvelproject.view.detail.adapter.CarouselAdapter
import com.hectorfortuna.marvelproject.view.detail.decoration.BoundsOffsetDecoration
import com.hectorfortuna.marvelproject.view.detail.decoration.LinearHorizontalSpacingDecoration
import com.hectorfortuna.marvelproject.view.detail.decoration.ProminentLayoutManager
import com.hectorfortuna.marvelproject.view.detail.viewmodel.DetailViewModel
import kotlinx.coroutines.Dispatchers
import timber.log.Timber


class DetailFragment : Fragment() {
    lateinit var viewModel: DetailViewModel
    lateinit var repository: DatabaseRepository
    lateinit var categoryRepository: CategoryRepository
    private var checkCharacter: Boolean = false
    private lateinit var snapHelper: SnapHelper
    private lateinit var carouselAdapter: CarouselAdapter
    private lateinit var binding: CharacterDetailBinding
    private lateinit var favorite: Favorites
    private lateinit var user: User
    private val dao: CharacterDAO by lazy {
        AppDatabase.getDb(requireContext()).characterDao()
    }


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
        repository = DatabaseRepositoryImpl(dao)
        categoryRepository = CategoryRepositoryImpl(ApiService.service)
        viewModel = DetailViewModel.DetailViewModelProviderFactory(
            repository, Dispatchers.IO, categoryRepository
        ).create(DetailViewModel::class.java)

        getUserByIntent()

        viewModel.verifySavedCharacter(favorite.id, user.email)
        getCategory(id = favorite.id, category = "comics")

        binding.run {
            setImage(imgDetailsPrincipal)

            txtDetails.text = favorite.name
            txtDescription.text = favorite.description

            setFavoriteCharacter()
        }

        observeVMEvents()
    }

    private fun getUserByIntent() {
        activity?.let {
            user = it.intent.getParcelableExtra<User>("USER") as User
        }
    }

    private fun CharacterDetailBinding.setFavoriteCharacter() {
        fabDetails.setOnClickListener {

            checkCharacter = if (checkCharacter) {
                val newFavorite = favorite.copy(email = user.email)
                viewModel.deleteCharacter(newFavorite)
                fabDetails.setImageResource(R.drawable.ic_full_favourite)
                false
            } else {
                val copyFavorite = favorite.copy(email = user.email)
                viewModel.insertFavorite(copyFavorite)
                fabDetails.setImageResource(R.drawable.ic_favorite)
                true
            }
        }
    }

    private fun getCategory(id: Long, category: String) {
        val ts = ts()
        viewModel.getCategory(apiKey(), hash(ts), ts.toLong(), id, category)
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
        viewModel.categoryResponse.observe(viewLifecycleOwner) {
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
        viewModel.verifyCharacter.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { exist ->
                        if (exist) {
                            binding.fabDetails.setImageResource(R.drawable.ic_favorite)
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
        carouselAdapter = CarouselAdapter(list)
    }


    private fun setRecycler(list: List<Result>) {
        setAdapter(list)
        snapHelper = PagerSnapHelper()

        binding.imgPoster.apply {
            adapter = carouselAdapter
            layoutManager = ProminentLayoutManager(context)
            setItemViewCacheSize(4)
            val spacing = resources.getDimensionPixelSize(R.dimen.carousel_spacing)
            addItemDecoration(LinearHorizontalSpacingDecoration(spacing))
            addItemDecoration(BoundsOffsetDecoration())
        }

        snapHelper.attachToRecyclerView(binding.imgPoster)
    }

    private fun setImage(image: AppCompatImageView) {
        Glide.with(this@DetailFragment)
            .load("${favorite.thumbnail.path}.${favorite.thumbnail.extension}")
            .centerCrop()
            .into(image)
    }
}