package com.shopify.testify;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

interface BitmapCompare {
    boolean compareBitmaps(@Nullable Bitmap baselineBitmap, @Nullable Bitmap currentBitmap);
}
