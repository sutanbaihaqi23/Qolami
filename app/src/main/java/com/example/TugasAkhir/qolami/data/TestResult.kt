package com.example.TugasAkhir.qolami.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "test_results")
data class TestResult(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String?,
    val bitmapDrawingView: ByteArray,
    val confidence: Int,
    val predictedLabel: String,
    val letter: String?,
    val leftLetter: String?,
    val middleLetter: String?,
    val rightLetter: String?,
    val leftLetterImg: Int?,
    val middleLetterImg: Int?,
    val rightLetterImg: Int?,
    val numberTest: Int,
    val isCorrect: Boolean,
    val historyNumber: Int,
    val finishedNumber : String,
    val score : String,
    val type: Int
)
