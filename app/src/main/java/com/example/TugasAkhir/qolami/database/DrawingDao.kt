package com.example.TugasAkhir.qolami.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.TugasAkhir.qolami.data.Drawing

@Dao
interface DrawingDao {
    @Insert
    suspend fun insert(drawing: Drawing)

    @Query("SELECT * FROM drawings WHERE user_id = :userId")
    suspend fun getDrawingsForUser(userId: String): List<Drawing>

    @Delete
    suspend fun delete(drawing: Drawing)
}