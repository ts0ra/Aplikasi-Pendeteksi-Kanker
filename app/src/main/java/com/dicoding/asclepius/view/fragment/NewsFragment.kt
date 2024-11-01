package com.dicoding.asclepius.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.data.Result
import com.dicoding.asclepius.databinding.FragmentNewsBinding
import com.dicoding.asclepius.view.adapter.NewsAdapter
import com.dicoding.asclepius.view.viewmodel.NewsViewModel
import com.dicoding.asclepius.view.viewmodel.ViewModelFactory

class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<NewsViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }
    private val newsAdapter = NewsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }

        viewModel.getNews().observe(viewLifecycleOwner) { result ->
           when (result) {
               is Result.Loading -> {
                   binding.progressBar.visibility = View.VISIBLE
                   binding.emptyNewsTv.visibility = View.GONE
               }
               is Result.Error -> {
                   binding.progressBar.visibility = View.GONE
                   binding.emptyNewsTv.visibility = View.VISIBLE
                   Toast.makeText(requireActivity(), result.error, Toast.LENGTH_SHORT).show()
               }
               is Result.Success -> {
                   binding.progressBar.visibility = View.GONE
                   if (result.data.isEmpty()) {
                       binding.emptyNewsTv.visibility = View.VISIBLE
                   } else {
                       newsAdapter.submitList(result.data)
                       binding.emptyNewsTv.visibility = View.GONE
                   }
               }
           }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}