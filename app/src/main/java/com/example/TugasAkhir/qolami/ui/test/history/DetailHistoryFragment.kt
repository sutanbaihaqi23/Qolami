package com.example.TugasAkhir.qolami.ui.test.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.TugasAkhir.qolami.R
import com.example.TugasAkhir.qolami.adapter.DetailHistoryAdapter
import com.example.TugasAkhir.qolami.databinding.FragmentDetailHistoryBinding
import com.example.TugasAkhir.qolami.viewmodel.AuthViewModel
import com.example.TugasAkhir.qolami.viewmodel.TestResultViewModel


class DetailHistoryFragment : Fragment() {
    private lateinit var binding: FragmentDetailHistoryBinding
    private lateinit var viewModel: TestResultViewModel
    private lateinit var adapter: DetailHistoryAdapter
    private lateinit var userId: String
    private lateinit var authViewModel: AuthViewModel

    private lateinit var historyNumber: String
    private lateinit var finishedNumber: String
    private lateinit var score: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailHistoryBinding.inflate(inflater, container, false)
        adapter = DetailHistoryAdapter(emptyList())

        arguments?.let {
            historyNumber = it.getInt("historyNumber").toString()
            finishedNumber = it.getString("finishedNumber")!!
            score = it.getString("score")!!

        }
        binding.tvFinished.text = "$finishedNumber"
        binding.tvScore.text = "$score"
        binding.tvNumberHistory.text = "$historyNumber"

        viewModel = ViewModelProvider(this).get(TestResultViewModel::class.java)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
        userId = authViewModel.getUserUid() ?: ""
        viewModel.getTestResultsForUserAndHistoryNumber(userId, historyNumber)

        viewModel.testResults.observe(viewLifecycleOwner, Observer { results ->
            adapter.submitList(results)
        })

        //buat handle untuk fragment back dari tombol device
        requireActivity().onBackPressedDispatcher.addCallback {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, ListHistoryFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.rvDetailHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDetailHistory.adapter = adapter

        binding.icBackToHomeTest.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, ListHistoryFragment())
                .addToBackStack(null)
                .commit()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}
