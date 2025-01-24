package com.example.TugasAkhir.qolami.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.TugasAkhir.qolami.R
import com.example.TugasAkhir.qolami.data.TempResultTest
import com.example.TugasAkhir.qolami.data.TempResultTest2
import com.example.TugasAkhir.qolami.data.TestResult
import com.example.TugasAkhir.qolami.databinding.ItemResultTest2Binding
import com.example.TugasAkhir.qolami.databinding.ItemResultTestBinding

class DetailHistoryAdapter(
    private var resultList: List<TestResult>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_RESULT_TEST = 0
        private const val TYPE_RESULT_TEST2 = 1
    }

    // Fungsi untuk menentukan tipe tampilan berdasarkan data
    override fun getItemViewType(position: Int): Int {
        return when (resultList[position].type) {
            0 -> TYPE_RESULT_TEST
            1 -> TYPE_RESULT_TEST2
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    // Membuat ViewHolder yang sesuai dengan tipe
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_RESULT_TEST -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_result_test, parent, false)
                ResultTestViewHolder(view)
            }
            TYPE_RESULT_TEST2 -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_result_test2, parent, false)
                ResultTest2ViewHolder(view)
            }
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    // Mengikat data ke ViewHolder berdasarkan tipe
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = resultList[position]
        when (holder) {
            is ResultTestViewHolder -> holder.bind(item)
            is ResultTest2ViewHolder -> holder.bind(item)
        }
    }

    // Menentukan jumlah item dalam list
    override fun getItemCount(): Int = resultList.size

    // Fungsi untuk memperbarui data adapter
    fun submitList(list: List<TestResult>) {
        resultList = list
        notifyDataSetChanged()
    }

    // ViewHolder untuk TYPE_RESULT_TEST
    class ResultTestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNumberQuestion: TextView = itemView.findViewById(R.id.tv_number_question)
        private val imgBitmap: ImageView = itemView.findViewById(R.id.img_bitmap)
        private val tvLetter: TextView = itemView.findViewById(R.id.tv_letter)
        private val tvAccuracy: TextView = itemView.findViewById(R.id.tv_accuracy)
        private val tvBooleanQuestion: TextView = itemView.findViewById(R.id.tv_boolean_question)
        private val tvPredictedLabel: TextView = itemView.findViewById(R.id.tv_predicted_label)


        fun bind(item: TestResult) {
            tvNumberQuestion.text = item.numberTest.toString()
            val bitmap = BitmapFactory.decodeByteArray(item.bitmapDrawingView, 0, item.bitmapDrawingView.size)
            imgBitmap.setImageBitmap(bitmap)
            tvBooleanQuestion.text = if (item.isCorrect) "Benar !" else "Salah !"
            tvPredictedLabel.text = "Prediksi Huruf : "+ item.predictedLabel
            tvLetter.text = item.letter
            tvAccuracy.text = "Skor : "+(item.confidence)+"%"
        }
    }

    // ViewHolder for TempResultTest2
    class ResultTest2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNumberQuestion: TextView = itemView.findViewById(R.id.tv_number_question)
        private val imgBitmap: ImageView = itemView.findViewById(R.id.img_bitmap)
        private val tvLetter: TextView = itemView.findViewById(R.id.tv_letter)
        private val tvAccuracy: TextView = itemView.findViewById(R.id.tv_accuracy)
        private val tvBooleanQuestion: TextView = itemView.findViewById(R.id.tv_boolean_question)
        private val tvPredictedLabel: TextView = itemView.findViewById(R.id.tv_predicted_label)

        private val imgLastLetterQuestion: ImageView = itemView.findViewById(R.id.img_last_letter_question)
        private val imgMiddleLetterQuestion: ImageView = itemView.findViewById(R.id.img_middle_letter_question)
        private val imgStartLetterQuestion: ImageView = itemView.findViewById(R.id.img_start_letter_question)

        fun bind(item: TestResult) {
            tvNumberQuestion.text = item.numberTest.toString()
            val bitmap = BitmapFactory.decodeByteArray(item.bitmapDrawingView, 0, item.bitmapDrawingView.size)
            imgBitmap.setImageBitmap(bitmap)
            tvLetter.text = "${item.middleLetter}"
            tvAccuracy.text = "Skor "+(item.confidence)+"%"
            tvBooleanQuestion.text = if (item.isCorrect) "Benar !" else "Salah !"
            tvPredictedLabel.text = "Prediksi Huruf : "+ item.predictedLabel

            item.leftLetterImg?.let { imgLastLetterQuestion.setImageResource(it) }
            item.middleLetterImg?.let { imgMiddleLetterQuestion.setImageResource(it) }
            item.rightLetterImg?.let { imgStartLetterQuestion.setImageResource(it) }
        }
    }
}
