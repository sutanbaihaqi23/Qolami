package com.example.TugasAkhir.qolami.customview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private var path = android.graphics.Path()
    private val paint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 20f
        isAntiAlias = true
    }
    private var canvasBitmap: Bitmap? = null
    private var drawCanvas: Canvas? = null

    init {
        setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        drawCanvas = Canvas(canvasBitmap!!)
    }

    override fun onDraw(canvas: Canvas) { // Non-nullable Canvas
        super.onDraw(canvas)
        canvas.drawBitmap(canvasBitmap!!, 0f, 0f, null)
        canvas.drawPath(path, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x
        val touchY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> path.moveTo(touchX, touchY)
            MotionEvent.ACTION_MOVE -> path.lineTo(touchX, touchY)
            MotionEvent.ACTION_UP -> drawCanvas?.drawPath(path, paint)
        }
        invalidate()
        return true
    }

    fun clearCanvas() {
        path.reset()
        canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        drawCanvas = Canvas(canvasBitmap!!)
        invalidate()
    }
}
