package io.github.grishaninvyacheslav.polus_dispatcher.ui.custom_views

import android.R
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isVisible
import io.github.grishaninvyacheslav.polus_dispatcher.databinding.ViewProgressButtonBinding

class ProgressButton : FrameLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val attributesNames = intArrayOf(R.attr.text)
        val attributesValue: TypedArray = context.obtainStyledAttributes(attrs, attributesNames)
        val text: CharSequence = attributesValue.getText(0)
        attributesValue.recycle()
        addView(
            binding.apply {
                button.text = text
            }.root
        )
    }

    private val binding =
        ViewProgressButtonBinding.inflate(LayoutInflater.from(context)).apply {
            button.setOnClickListener {
                buttonClickListener?.invoke(it)
            }
        }

    var buttonClickListener: ((view: View) -> Unit)? = null

    var isLoading: Boolean = false
        set(value) {
            binding.progressBar.isVisible = value
            field = value
        }
}