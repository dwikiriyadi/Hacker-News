package io.gitlab.dwikiriyadi.hackernews.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import io.gitlab.dwikiriyadi.hackernews.BR
import io.gitlab.dwikiriyadi.hackernews.R

import io.gitlab.dwikiriyadi.hackernews.SharedViewModel
import io.gitlab.dwikiriyadi.hackernews.data.model.NetworkState
import io.gitlab.dwikiriyadi.hackernews.databinding.CommentItemBinding
import io.gitlab.dwikiriyadi.hackernews.databinding.DetailFragmentBinding

class DetailFragment : Fragment() {

//    private lateinit var viewModel: DetailViewModel

    private val sharedModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DetailFragmentBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@DetailFragment
            model = sharedModel
        }

        sharedModel.detailError.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

        sharedModel.detailNetworkState.observe(viewLifecycleOwner, Observer {
            when (it) {
                NetworkState.LOADING -> binding.progressHorizontal.visibility = View.VISIBLE
                else -> binding.progressHorizontal.visibility = View.GONE
            }
        })

        binding.favorite.setOnClickListener {
            sharedModel.addToFavorite(sharedModel.story.value!!.id)
        }

        sharedModel.comments.observe(viewLifecycleOwner, Observer {comments ->
            comments.map {
                val comment = CommentItemBinding.inflate(inflater, binding.commentContainer, false)
                comment.setVariable(BR.data, it)

                binding.commentContainer.addView(comment.root)
            }

        })

        return binding.root
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

}
