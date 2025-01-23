package com.example.TugasAkhir.qolami.util

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Log
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

object ModelUtil {

    private const val IMG_SIZE = 64
    private const val OUTPUT_CLASSES =30


    fun loadModel(context: Context, modelFileName: String): Interpreter {
        val assetManager = context.assets
        val fileDescriptor = assetManager.openFd(modelFileName)
        val inputStream = fileDescriptor.createInputStream()
        val fileChannel = inputStream.channel
        val mappedByteBuffer = fileChannel.map(
            java.nio.channels.FileChannel.MapMode.READ_ONLY,
            fileDescriptor.startOffset,
            fileDescriptor.declaredLength
        )
        Log.d("ModelUtil", "Model loaded successfully: $modelFileName")
        return Interpreter(mappedByteBuffer)
    }


    fun processImage(bitmap: Bitmap): ByteBuffer {
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, IMG_SIZE, IMG_SIZE, true)
        val tensorImage = TensorImage.fromBitmap(resizedBitmap)
        val buffer = ByteBuffer.allocateDirect(4 * IMG_SIZE * IMG_SIZE)  // Float 4 bytes
        buffer.order(ByteOrder.nativeOrder())
        val pixels = IntArray(IMG_SIZE * IMG_SIZE)
        resizedBitmap.getPixels(pixels, 0, IMG_SIZE, 0, 0, IMG_SIZE, IMG_SIZE)

        for (pixel in pixels) {
            val r = (pixel shr 16) and 0xFF
            val g = (pixel shr 8) and 0xFF
            val b = pixel and 0xFF
            val grayscale = (0.2989 * r + 0.587 * g + 0.114 * b).toFloat() / 255.0f
            buffer.putFloat(grayscale)
        }
        return buffer
    }

    fun getAccuracyForTargetIndex(interpreter: Interpreter, byteBuffer: ByteBuffer, targetIndex: Int): Int {
        val output = Array(1) { FloatArray(OUTPUT_CLASSES) } // Output sesuai jumlah kelas
        interpreter.run(byteBuffer, output)

        val probabilities = output[0]
        val confidence = (probabilities.getOrElse(targetIndex) { 0.0f } * 100).toInt() // Konversi ke persen dan bilangan bulat
        Log.d("ModelUtil", "Target Index: $targetIndex, Confidence: $confidence%")

        return confidence
    }
    fun getAccuracyForTargetIndexandLabel(
        interpreter: Interpreter,
        byteBuffer: ByteBuffer,
        targetIndex: Int
    ): Pair<Int, String> {
        val output = Array(1) { FloatArray(OUTPUT_CLASSES) } // Output sesuai jumlah kelas
        interpreter.run(byteBuffer, output)

        val probabilities = output[0]
        val confidence = (probabilities.getOrElse(targetIndex) { 0.0f } * 100).toInt() // Konversi ke persen dan bilangan bulat

        // Cari indeks dengan probabilitas tertinggi
        val predictedIndex = probabilities.indices.maxByOrNull { probabilities[it] } ?: -1
        val predictedLabel = if (predictedIndex in classLabels.indices) {
            classLabels[predictedIndex]
        } else {
            "Unknown"
        }

        Log.d("ModelUtil", "Target Index: $targetIndex, Confidence: $confidence%, Predicted Label: $predictedLabel")

        return confidence to predictedLabel
    }




    fun runInference(interpreter: Interpreter, byteBuffer: ByteBuffer): String {
        val output = Array(1) { FloatArray(OUTPUT_CLASSES) }  // Output sesuai jumlah kelas
        interpreter.run(byteBuffer, output)

        val predictedIndex = output[0].indices.maxByOrNull { output[0][it] } ?: -1
        Log.d("ModelUtil", "Prediction: $predictedIndex")
        return getClassLabel(predictedIndex)
    }

    fun runInferenceWithConfidence(interpreter: Interpreter, byteBuffer: ByteBuffer): Pair<String, Float> {
        val output = Array(1) { FloatArray(OUTPUT_CLASSES) }
        interpreter.run(byteBuffer, output)

        val probabilities = output[0]
        val predictedIndex = probabilities.indices.maxByOrNull { probabilities[it] } ?: -1
        val confidence = probabilities.getOrElse(predictedIndex) { 0.0f }
        Log.d("ModelUtil", "Prediction: $predictedIndex, Confidence: $confidence")

        val predictedLabel = getClassLabel(predictedIndex)
        return Pair(predictedLabel, confidence)
    }

    fun runInferenceAndLogConfidence(interpreter: Interpreter, byteBuffer: ByteBuffer): Pair<String, Float> {
        val output = Array(1) { FloatArray(OUTPUT_CLASSES) }  // Output sesuai jumlah kelas
        interpreter.run(byteBuffer, output)

        val probabilities = output[0]

        // Logging semua indeks dan confidence level
        probabilities.forEachIndexed { index, confidence ->
            Log.d("ModelUtil", "Index: $index, Confidence: ${confidence * 100}%")
        }

        // Menentukan prediksi terbaik
        val predictedIndex = probabilities.indices.maxByOrNull { probabilities[it] } ?: -1
        val confidence = probabilities.getOrElse(predictedIndex) { 0.0f }  // Confidence dari prediksi terbaik
        val predictedLabel = getClassLabel(predictedIndex)

        Log.d("ModelUtil", "Predicted Label: $predictedLabel, Confidence: ${confidence * 100}%")

        return Pair(predictedLabel, confidence)
    }

    fun getConfidenceForIndex(interpreter: Interpreter, byteBuffer: ByteBuffer, targetIndex: Int): Float {
        val output = Array(1) { FloatArray(OUTPUT_CLASSES) } // Output sesuai jumlah kelas
        interpreter.run(byteBuffer, output)

        val probabilities = output[0]

        // Validasi indeks target
        if (targetIndex < 0 || targetIndex >= OUTPUT_CLASSES) {
            Log.e("ModelUtil", "Invalid target index: $targetIndex")
            return -1.0f // Indikasi kesalahan, indeks di luar jangkauan
        }

        val confidence = probabilities[targetIndex]
        Log.d("ModelUtil", "Confidence for index $targetIndex (${getClassLabel(targetIndex)}): ${confidence * 100}%")

        return confidence
    }

    fun checkTargetIndex(interpreter: Interpreter, byteBuffer: ByteBuffer, targetIndex: Int): String {
        val output = Array(1) { FloatArray(OUTPUT_CLASSES) } // Output sesuai jumlah kelas
        interpreter.run(byteBuffer, output)

        val probabilities = output[0]
        val predictedIndex = probabilities.indices.maxByOrNull { probabilities[it] } ?: -1
        val confidence = probabilities.getOrElse(predictedIndex) { 0.0f }

        return if (predictedIndex == targetIndex) {
            "${confidence * 100}%"
        } else {
            "Wrong! Predicted"
        }
    }






    private val classLabels= arrayOf(
        "Kho", "Tsa", "Dzal", "Kha", "Ta","Jim","Dal","Ra","Ba","Tzo","Ain",
        "Za","Tho","Ghain","Fa","Shod","Sin","Syin","Dhod","Qaf","Ha","Nun","Lam",
        "Kaf","Mim","Wau","Ya","Hamzah","Lam Alif","Alif"
    )

    private fun getClassLabel(index: Int): String {
        return classLabels.getOrElse(index) { "Unknown" }
    }

}

