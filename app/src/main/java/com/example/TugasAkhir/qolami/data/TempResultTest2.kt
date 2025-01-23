package com.example.TugasAkhir.qolami.data

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TempResultTest2(
    val bitmapDrawingView: Bitmap,
    val confidence: Int,
    val predictedLabel: String,
    val leftLetter:String,
    val middleLetter:String,
    val rightLetter:String,
    val leftLetterImg: Int,
    val middleLetterImg: Int,
    val rightLetterImg: Int,
    val numberTest : Int,
    val isCorrect: Boolean
) : Parcelable