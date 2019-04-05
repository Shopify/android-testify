/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2019 Shopify Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.shopify.testify.sample.clients.details

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.view_client_details.view.address as addressView
import kotlinx.android.synthetic.main.view_client_details.view.address_row as addressRowViewGroup
import kotlinx.android.synthetic.main.view_client_details.view.heading as headingView
import kotlinx.android.synthetic.main.view_client_details.view.phone as phoneView
import kotlinx.android.synthetic.main.view_client_details.view.phone_row as phoneRowViewGroup
import kotlinx.android.synthetic.main.view_client_details.view.profile_image as profileImage

class ClientDetailsView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {

    fun render(state: ClientDetailsViewState) {
        profileImage.setImageResource(state.avatar)
        headingView.text = state.heading

        addressRowViewGroup.isVisible = state.address != null
        addressView.text = state.address

        phoneRowViewGroup.isVisible = state.phoneNumber != null
        phoneView.text = state.phoneNumber
    }
}