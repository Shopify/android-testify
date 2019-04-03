package com.shopify.testify

import android.graphics.Bitmap

internal interface BitmapCompare {
    fun compareBitmaps(baselineBitmap: Bitmap?, currentBitmap: Bitmap?): Boolean
}
