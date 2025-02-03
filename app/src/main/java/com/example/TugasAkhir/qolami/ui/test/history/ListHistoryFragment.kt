package com.example.TugasAkhir.qolami.ui.test.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.TugasAkhir.qolami.R
import com.example.TugasAkhir.qolami.adapter.ListHistoryAdapter
import com.example.TugasAkhir.qolami.data.ListHistoryItem
import com.example.TugasAkhir.qolami.database.AppDatabase
import com.example.TugasAkhir.qolami.databinding.FragmentHistoryBinding
import com.example.TugasAkhir.qolami.ui.test.WritingTestHomeFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


class ListHistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var adapter: ListHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentHistoryBinding.inflate(inflater, container, false)
        binding.rvListHistory
        fetchData()
        binding.icBackToHomeTest.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, WritingTestHomeFragment())
                .addToBackStack(null)
                .commit()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Handle tombol back dari perangkat
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, WritingTestHomeFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun fetchData() {
        viewLifecycleOwner.lifecycleScope.launch {
            val database = AppDatabase.getDatabase(requireContext())
            val userId = FirebaseAuth.getInstance().currentUser?.uid

            if (userId != null) {
                val listHistoryItems = database.testResultDao().getSummaryForUser(userId)
                listHistoryItems.forEach {
                    Log.d("ListHistoryFragment", "HistoryNumber: ${it.historyNumber}")
                }
                binding.rvListHistory.visibility = View.VISIBLE
                updateRecyclerView(listHistoryItems)
            } else {
                Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun updateRecyclerView(items: List<ListHistoryItem>) {
        val sortedItems = items.sortedBy { it.historyNumber } // Urutkan descending
        binding.rvListHistory.layoutManager = LinearLayoutManager(requireContext())
        adapter = ListHistoryAdapter(sortedItems) { item ->
            // Navigasi ke DetailHistoryFragment
            val detailFragment = DetailHistoryFragment()
            val bundle = Bundle().apply {
                putInt("historyNumber", item.historyNumber) // Ubah ke Long
                putString("finishedNumber", item.finishedNumber)
                putString("score", item.score.toString())
            }
            detailFragment.arguments = bundle
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, detailFragment)
                .addToBackStack(null)
                .commit()
        }
        binding.rvListHistory.adapter = adapter
    }

}