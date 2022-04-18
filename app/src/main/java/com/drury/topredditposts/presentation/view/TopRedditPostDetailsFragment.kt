package com.drury.topredditposts.presentation.view

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.drury.topredditposts.R
import com.drury.topredditposts.databinding.FragmentRedditPostDetailsBinding
import com.drury.topredditposts.domain.model.RedditPost
import com.drury.topredditposts.presentation.viewmodel.TopRedditPostDetailsViewModel
import com.drury.topredditposts.utils.show
import org.koin.androidx.viewmodel.ext.android.viewModel

class TopRedditPostDetailsFragment : BaseFragment() {

    private var _binding : FragmentRedditPostDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel : TopRedditPostDetailsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRedditPostDetailsBinding.inflate(inflater)

        arguments?.getParcelable<RedditPost>(PARAMETER_REDDIT_POST_KEY)?.let { redditPost ->
            binding.redditPostTitle.text = redditPost.title
            binding.redditPostDescription.text = redditPost.name
            binding.redditPostUrl.apply {
                text = getString(R.string.top_reddit_post_url, redditPost.url ?: "")
                setOnClickListener { shareUrl(redditPost.url ?: "") }
            }
            // Only load images if image is PNG or JPG
            if (viewModel.shouldLoadImage(redditPost)) {
                Glide.with(this)
                    .asBitmap()
                    .load(redditPost.thumbnail)
                    .centerCrop()
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            binding.thumbnail.setImageBitmap(resource)
                            binding.saveThumbnailButton.apply {
                                show()
                                setOnClickListener {
                                    saveImageOnGallery(resource)
                                }
                            }
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                            // do nothing
                        }

                    })
            }
        }

        return binding.root
    }

    private val backPressedDispatcher = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            this@TopRedditPostDetailsFragment.onBackPressed()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        (requireActivity() as MainActivity).apply {
            onBackPressedDispatcher.addCallback(viewLifecycleOwner, backPressedDispatcher)
            setupActionBarWithNavController(findNavController())
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            this.onBackPressed()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun onBackPressed() {
        findNavController().popBackStack()
    }

    private fun shareUrl(url: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, url)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    // I would move this to an UseCase, but we have to keep Android classes away from below layers
    private fun saveImageOnGallery(bitmap: Bitmap) {
        requireActivity().contentResolver
        val savedImageURL = MediaStore.Images.Media.insertImage(
            requireActivity().contentResolver,
            bitmap,
            bitmap.toString(),
            "Image"
        )
        val success = savedImageURL != null
        showToastSaveImage(success)
    }

    private fun showToastSaveImage(success: Boolean) {
        Toast.makeText(context, if (success) R.string.top_reddit_post_save_image_success else R.string.top_reddit_post_save_image_error, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        backPressedDispatcher.remove()
        super.onDestroyView()
    }


    companion object {

        const val TAG = "TopRedditPostDetailsFragment"
        const val PARAMETER_REDDIT_POST_KEY = "PARAMETER_REDDIT_POST_KEY"

        fun newInstance() : TopRedditPostDetailsFragment = TopRedditPostDetailsFragment()
    }
}