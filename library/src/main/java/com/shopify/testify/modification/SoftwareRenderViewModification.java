package com.shopify.testify.modification;

import android.view.View;
import android.widget.ImageView;

public class SoftwareRenderViewModification extends ViewModification {

    public SoftwareRenderViewModification() {
        super(false);
    }

    @Override
    protected void performModification(View view) {
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected boolean qualifies(View view) {
        return (view instanceof ImageView);
    }
}
