package com.drury.topredditposts.presentation.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drury.topredditposts.R
import com.drury.topredditposts.databinding.FragmentRedditPostListBinding
import com.drury.topredditposts.presentation.view.TopRedditPostDetailsFragment.Companion.PARAMETER_REDDIT_POST_KEY
import com.drury.topredditposts.presentation.view.adapter.TopRedditPostAdapter
import com.drury.topredditposts.presentation.viewmodel.MainListViewModel
import com.drury.topredditposts.utils.gone
import com.drury.topredditposts.utils.show
import org.koin.androidx.viewmodel.ext.android.viewModel


class TopRedditPostFragment : BaseFragment() {

    private var _binding : FragmentRedditPostListBinding? = null
    private val binding get() = _binding!!

    private val viewModel : MainListViewModel by viewModel()
    private val adapter = TopRedditPostAdapter({ redditPost ->
        val bundle = Bundle()
        bundle.putParcelable(PARAMETER_REDDIT_POST_KEY, redditPost)
        NavHostFragment.findNavController(this)
            .navigate(R.id.reddit_post_details, bundle)
    }, { redditPost -> viewModel.shouldLoadImage(redditPost) })
    private var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRedditPostListBinding.inflate(inflater)
        setupView()
        return binding.root
    }

    private fun setupView() {
        setupBackButton()
        binding.posts.layoutManager = LinearLayoutManager(requireContext())
        binding.posts.adapter = adapter
        binding.posts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val linearLayoutManager = recyclerView.layoutManager as? LinearLayoutManager

                if (!isLoading) {
                    if ((linearLayoutManager?.findLastCompletelyVisibleItemPosition() ?: -1) >= (linearLayoutManager?.itemCount ?: -1) - 1) {
                        adapter.getLastItemId()?.let {
                            isLoading = true
                            viewModel.getMoreTopPosts(it)
                        }
                    }
                }
            }
        })
        binding.errorText.setOnClickListener {
            viewModel.getInitialTopPosts()
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getInitialTopPosts()
        }
    }

    private fun setupObservers() {
        viewModel.mutableTopPostsListLiveData.observe(viewLifecycleOwner, Observer { postList ->
            binding.swipeRefreshLayout.isRefreshing = false
            isLoading = false
            adapter.setItems(postList)
        })

        viewModel.mutableTopPostsNewListLiveData.observe(viewLifecycleOwner, Observer { newList ->
            binding.swipeRefreshLayout.isRefreshing = false
            isLoading = false
            adapter.addItems(newList)
        })

        viewModel.mutableIsLoadingMoreDataLiveData.observe(viewLifecycleOwner, Observer { isLoading ->
            binding.loader.visibility = if (isLoading) View.VISIBLE else View.GONE
        })
        viewModel.screenStateLiveData.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                MainListViewModel.ScreenState.Content -> {
                    binding.loaderList.gone()
                    binding.errorText.gone()
                    binding.posts.show()
                }
                MainListViewModel.ScreenState.Loading -> {
                    if (!binding.swipeRefreshLayout.isRefreshing) {
                        binding.loaderList.show()
                    } else {
                        binding.loaderList.gone()
                    }
                    binding.errorText.gone()
                    binding.posts.gone()
                }
                MainListViewModel.ScreenState.Error -> {
                    binding.loaderList.gone()
                    binding.errorText.show()
                    binding.posts.gone()
                }
            }
        })
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        viewModel.getInitialTopPosts()
    }

    companion object {
        const val TAG = "TopRedditPostFragment"
        fun newInstance() : TopRedditPostFragment = TopRedditPostFragment()
    }
}