package io.gitlab.dwikiriyadi.hackernews.ui

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import io.gitlab.dwikiriyadi.hackernews.R
import io.gitlab.dwikiriyadi.hackernews.SharedViewModel
import io.gitlab.dwikiriyadi.hackernews.SharedViewModelFactory
import io.gitlab.dwikiriyadi.hackernews.data.model.NetworkState
import io.gitlab.dwikiriyadi.hackernews.databinding.MainFragmentBinding
import io.gitlab.dwikiriyadi.hackernews.ui.adapter.HackerNewsAdapter
import io.gitlab.dwikiriyadi.hackernews.util.MarginItemDecoration

class MainFragment : Fragment() {
//    private lateinit var viewModel: MainViewModel
    private val sharedModel: SharedViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = MainFragmentBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@MainFragment
            model = sharedModel
        }

        sharedModel.error.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

        sharedModel.networkState.observe(viewLifecycleOwner, Observer {
            when (it) {
                NetworkState.LOADING -> binding.progressHorizontal.visibility = View.VISIBLE
                else -> binding.progressHorizontal.visibility = View.GONE
            }
        })

        initRecyclerView(binding)

        sharedModel.getData()

        return binding.root
    }

    private fun initRecyclerView(binding: MainFragmentBinding) {
        val adapter = HackerNewsAdapter {
            sharedModel.getStory(it)
            findNavController().navigate(R.id.action_mainFragment_to_detailFragment)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(MarginItemDecoration(16))
            this.adapter = adapter
        }

        sharedModel.stories.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

}
