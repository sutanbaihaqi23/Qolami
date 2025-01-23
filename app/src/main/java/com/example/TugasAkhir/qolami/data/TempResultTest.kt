package com.example.TugasAkhir.qolami.data

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TempResultTest(
    val bitmapDrawingView: Bitmap,
    val confidence: Int,
    val predictedLabel: String,
    val letter: String,
    val numberTest : Int,
    val isCorrect: Boolean
) : Parcelable