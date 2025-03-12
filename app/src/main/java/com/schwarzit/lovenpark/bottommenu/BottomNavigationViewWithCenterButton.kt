package com.schwarzit.lovenpark.bottommenu

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.schwarzit.lovenpark.R


class BottomNavigationViewWithCenterButton(context: Context, attrs: AttributeSet) :
    ConstraintLayout(context, attrs) {
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)


        canvas?.let {
            val width = canvas.width.toFloat()
            val height = canvas.height.toFloat()
            val bumpRadius = 300f

            val backgroundPaint = Paint()

            backgroundPaint.color = context.getColor(R.color.fern_green)
            backgroundPaint.style = Paint.Style.FILL


            val paintWithShadow = Paint(backgroundPaint)
            paintWithShadow.setShadowLayer(10f, 0f, 0f, Color.GRAY)
            setLayerType(LAYER_TYPE_SOFTWARE, paintWithShadow);

            val topEdgeY = bumpRadius / 2

            // Main rounded rect background
            canvas?.drawRoundRect(
                0f,
                topEdgeY,
                width,
                height,
                40f,
                40f,
                paintWithShadow
            )

            // Signal button bump background
            canvas?.drawRoundRect(
                (width / 2) - (bumpRadius / 2),
                bumpRadius / 4,
                (width / 2) + (bumpRadius / 2),
                bumpRadius + (bumpRadius / 4),
                bumpRadius,
                bumpRadius,
                backgroundPaint
            )
        }
    }

}