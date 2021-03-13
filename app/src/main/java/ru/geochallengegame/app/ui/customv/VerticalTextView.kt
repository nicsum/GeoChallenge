package ru.geochallengegame.app.ui.customv

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.text.BoringLayout
import android.text.Layout
import android.text.TextUtils.TruncateAt
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.graphics.withSave


class VerticalTextView(context: Context, attrs: AttributeSet) : AppCompatTextView(context, attrs) {
    private val topDown = gravity.let { g ->
        !(Gravity.isVertical(g) && g.and(Gravity.VERTICAL_GRAVITY_MASK) == Gravity.BOTTOM)
    }
    private val metrics = BoringLayout.Metrics()
    private var h = Resources.getSystem().displayMetrics.heightPixels
    private var padLeft = compoundPaddingLeft + h / 3
    private var padTop = extendedPaddingTop

    private var layout1: Layout? = null

    override fun setText(text: CharSequence, type: BufferType) {
        super.setText(text, type)
        layout1 = null

    }

    private fun makeLayout(): Layout {
        if (layout1 == null) {
            metrics.width = height
            paint.color = currentTextColor
            paint.drawableState = drawableState
            layout1 = BoringLayout.make(
                text,
                paint,
                metrics.width,
                Layout.Alignment.ALIGN_NORMAL,
                2f,
                0f,
                metrics,
                false,
                TruncateAt.END,
                height - compoundPaddingLeft - compoundPaddingRight
            )
            padLeft = compoundPaddingLeft + h / 3
            padTop = extendedPaddingTop
        }
        return layout1!!

    }

    override fun onDraw(c: Canvas) {
        if (layout == null)
            return
        c.withSave {
            if (topDown) {
                val fm = paint.fontMetrics
                translate(textSize - (fm.bottom + fm.descent), 0f)
                rotate(90f)
            } else {
                translate(textSize, height.toFloat())
                rotate(-90f)
            }
            translate(padLeft.toFloat(), padTop.toFloat())
            makeLayout().draw(this)
        }
    }
}