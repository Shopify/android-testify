package com.shopify.testify.internal.modification

import android.view.View
import android.widget.ImageView

class SoftwareRenderViewModification : ViewModification(false) {

    override fun performModification(view: View) {
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }

    override fun qualifies(view: View): Boolean {
        return view is ImageView
    }
}
