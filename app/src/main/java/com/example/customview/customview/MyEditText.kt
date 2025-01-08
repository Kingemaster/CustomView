package com.example.customview.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import com.example.customview.R

class MyEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : androidx.appcompat.widget.AppCompatEditText(context, attrs) {

    private var iconDrawable:Drawable? = null

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.MyEditText,
            0, 0).apply {

            try {
                iconDrawable = getDrawable(R.styleable.MyEditText_clearIcon)
            } finally {
                recycle()
            }
        }
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        val icon = if (isFocused && text?.isNotEmpty() == true) iconDrawable else null
        setCompoundDrawablesWithIntrinsicBounds(null,null,icon,null)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (isClickedClearIcon(event)) text?.clear()
        performClick()
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }


    private fun isClickedClearIcon(event: MotionEvent?):Boolean{
        event?.let { e ->
            iconDrawable?.let {
                if (e.action == KeyEvent.ACTION_UP &&
                    e.x > width - it.intrinsicWidth &&
                    e.x < width &&
                    e.y > (height - it.intrinsicHeight) / 2 &&
                    e.y < (height + it.intrinsicHeight) / 2)
                    return true
            }
        }
        return false
    }

}