package com.example.TugasAkhir.qolami.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.TugasAkhir.qolami.data.ListHistoryItem
import com.example.TugasAkhir.qolami.data.TestResult

@Dao
interface TestResultDao {
    @Insert
    suspend fun insertTestResult(testResult: TestResult)

    @Query("SELECT COUNT(DISTINCT userId) FROM test_results WHERE userId = :userId")
    suspend fun getHistoryCountForUser(userId: String): Int

    @Query("""
    SELECT * FROM test_results 
    WHERE userId = :userId AND historyNumber = :historyNumber
    ORDER BY numberTest ASC
""")
    suspend fun getTestResultsForUserAndHistoryNumber(userId: String, historyNumber: String): List<TestResult>

    @Query("SELECT * FROM test_results WHERE userId = :userId ORDER BY historyNumber ASC")
    suspend fun getTestResultsForUser(userId: String): List<TestResult>


    @Query("""
    SELECT historyNumber, finishedNumber, 
           COUNT(CASE WHEN isCorrect THEN 1 END) * 10 AS score
    FROM test_results
    WHERE userId = :userId
    GROUP BY historyNumber
    ORDER BY historyNumber ASC
""")
    suspend fun getSummaryForUser(userId: String): List<ListHistoryItem>

}
