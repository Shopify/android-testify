package com.shopify.testify.interfaces;

import android.view.View;
import android.view.ViewGroup;

@SuppressWarnings("WeakerAccess")
public interface ViewProvider {
    View getView(ViewGroup rootView);
}
