package com.dicoding.asclepius.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.data.database.local.entity.HistoryEntity
import com.dicoding.asclepius.databinding.FragmentHistoryBinding
import com.dicoding.asclepius.view.adapter.HistoryAdapter
import com.dicoding.asclepius.view.viewmodel.HistoryViewModel
import com.dicoding.asclepius.view.viewmodel.ViewModelFactory

class HistoryFragment : Fragment(), HistoryAdapter.OnItemButtonClickListener  {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HistoryViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }
    private val historyAdapter = HistoryAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.apply {
            adapter = historyAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }

        viewModel.getAllHistory().observe(viewLifecycleOwner) { result ->
            historyAdapter.submitList(result)
            if (result.isEmpty()) {
                binding.emptyHistoryTv.visibility = View.VISIBLE
            } else {
                binding.emptyHistoryTv.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemButtonClick(history: HistoryEntity) {
        viewModel.deleteHistory(history)
    }
}