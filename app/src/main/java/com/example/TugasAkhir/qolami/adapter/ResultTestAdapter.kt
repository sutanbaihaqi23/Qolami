package com.example.TugasAkhir.qolami.adapter

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.TugasAkhir.qolami.R
import com.example.TugasAkhir.qolami.data.TempResultTest
import com.example.TugasAkhir.qolami.data.TempResultTest2

class ResultTestAdapter(
    private val resultList: List<Parcelable>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_RESULT_TEST = 0
        private const val TYPE_RESULT_TEST2 = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (resultList[position]) {
            is TempResultTest -> TYPE_RESULT_TEST
            is TempResultTest2 -> TYPE_RESULT_TEST2
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = resultList[position]
        when (holder) {
            is ResultTestViewHolder -> holder.bind(item as TempResultTest)
            is ResultTest2ViewHolder -> holder.bind(item as TempResultTest2)
        }
    }

    override fun getItemCount(): Int {
        return resultList.size
    }

    // ViewHolder for TempResultTest
    class ResultTestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNumberQuestion: TextView = itemView.findViewById(R.id.tv_number_question)
        private val imgBitmap: ImageView = itemView.findViewById(R.id.img_bitmap)
        private val tvLetter: TextView = itemView.findViewById(R.id.tv_letter)
        private val tvAccuracy: TextView = itemView.findViewById(R.id.tv_accuracy)
        private val tvBooleanQuestion: TextView = itemView.findViewById(R.id.tv_boolean_question)
        private val tvPredictedLabel: TextView = itemView.findViewById(R.id.tv_predicted_label)


        fun bind(item: TempResultTest) {
            tvNumberQuestion.text = item.numberTest.toString()
            imgBitmap.setImageBitmap(item.bitmapDrawingView)
            tvBooleanQuestion.text = if (item.isCorrect) "Correct !" else "Incorrect !"
            tvPredictedLabel.text = "Predicted : "+ item.predictedLabel
            tvLetter.text = item.letter
            tvAccuracy.text = "Accuracy Predicted : "+(item.confidence)+"%"
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

        fun bind(item: TempResultTest2) {
            tvNumberQuestion.text = item.numberTest.toString()
            imgBitmap.setImageBitmap(item.bitmapDrawingView)
            tvLetter.text = "${item.middleLetter}"
            tvAccuracy.text = "Accuracy Predicted : "+(item.confidence)+"%"
            tvBooleanQuestion.text = if (item.isCorrect) "Correct !" else "Incorrect !"
            tvPredictedLabel.text = "Predicted : "+ item.predictedLabel

            imgLastLetterQuestion.setImageResource(item.leftLetterImg)
            imgMiddleLetterQuestion.setImageResource(item.middleLetterImg)
            imgStartLetterQuestion.setImageResource(item.rightLetterImg)
        }
    }
}
