package com.maikkkko1.android_base_arq.components.carousel

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.maikkkko1.android_base_arq.databinding.ComponentCarouselDefaultBinding

class HorizontalCarouselDefault @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding: ComponentCarouselDefaultBinding = ComponentCarouselDefaultBinding.inflate(LayoutInflater.from(context))

    init {
        // Draw view
        binding.root.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT
        )

        addView(binding.root)
    }

    fun getCarouselItemsListRef() = binding.carouselItemsList
}
