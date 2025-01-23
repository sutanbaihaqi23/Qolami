package com.example.TugasAkhir.qolami.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.TugasAkhir.qolami.data.TestResult
import com.example.TugasAkhir.qolami.database.AppDatabase
import com.example.TugasAkhir.qolami.database.TestResultDao
import kotlinx.coroutines.launch

class TestResultViewModel(application: Application) : AndroidViewModel(application) {

    private val testResultDao: TestResultDao = AppDatabase.getDatabase(application).testResultDao()
    private val _testResults = MutableLiveData<List<TestResult>>()
    val testResults: LiveData<List<TestResult>> get() = _testResults

    fun getTestResults(userId: String) {
        viewModelScope.launch {
            _testResults.value = testResultDao.getTestResultsForUser(userId)
        }
    }

    fun getTestResultsForUserAndHistoryNumber(userId: String, historyNumber: String) {
        viewModelScope.launch {
            _testResults.value = testResultDao.getTestResultsForUserAndHistoryNumber(userId, historyNumber)
        }
    }

}
