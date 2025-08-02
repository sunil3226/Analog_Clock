package com.example.analogclock

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.icu.util.Calendar
import android.util.AttributeSet
import android.view.View

class CustomAnalogClock(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint()
    private val calendar = Calendar.getInstance()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()
        val radius = (Math.min(width, height) / 2 * 0.9f).toFloat()
        val centerX = width / 2
        val centerY = height / 2

        paint.isAntiAlias = true

        // Draw clock circle
        paint.strokeWidth = 0.1f
        paint.style = Paint.Style.FILL
        paint.color = Color.WHITE
        canvas.drawCircle(centerX, centerY, radius, paint)

        // Draw numbers
        paint.style = Paint.Style.FILL
        paint.textSize = radius / 6
        paint.textAlign = Paint.Align.CENTER
        paint.color = Color.BLACK

        val textRadius = radius * 0.85f

        for (number in 1..12) {
            val angle = Math.toRadians(((number * 30) - 90).toDouble())  // 30 degrees between numbers, -90 to start at top
            val x = (centerX + textRadius * Math.cos(angle)).toFloat()
            val y = (centerY + textRadius * Math.sin(angle)).toFloat() + (paint.textSize / 3) // Adjust vertical position
            canvas.drawText(number.toString(), x, y, paint)
        }

        // Get time
        calendar.timeInMillis = System.currentTimeMillis()
        val hours = calendar.get(Calendar.HOUR)
        val minutes = calendar.get(Calendar.MINUTE)
        val seconds = calendar.get(Calendar.SECOND)

        // Calculate angles for hands
        val hourAngle = (hours + minutes / 60f) * 30f - 90f
        val minuteAngle = (minutes + seconds / 60f) * 6f - 90f
        val secondAngle = seconds * 6f - 90f

        paint.strokeWidth = 5f
        paint.color = Color.BLACK

        // Draw hour hand
        drawHand(canvas, centerX, centerY, radius * 0.5f, hourAngle, paint)

        // Draw minute hand
        paint.strokeWidth = 3f
        drawHand(canvas, centerX, centerY, radius * 0.8f, minuteAngle, paint)

        // Draw second hand
        paint.color = Color.RED
        paint.strokeWidth = 1f
        drawHand(canvas, centerX, centerY, radius * 1f, secondAngle, paint)

        // Redraw every second
        postInvalidateDelayed(1000)
    }

    private fun drawHand(canvas: Canvas, cx: Float, cy: Float, length: Float, angle: Float, paint: Paint) {
        val rad = Math.toRadians(angle.toDouble())  // Convert angle from degrees to radians
        val x = (cx + length * Math.cos(rad)).toFloat()  // Calculate end x-coordinate
        val y = (cy + length * Math.sin(rad)).toFloat()  // Calculate end y-coordinate
        canvas.drawLine(cx, cy, x, y, paint)  // Draw line from center (cx, cy) to (x, y)
    }

}
