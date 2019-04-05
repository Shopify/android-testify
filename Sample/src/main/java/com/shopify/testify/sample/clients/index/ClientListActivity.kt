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
package com.shopify.testify.sample.clients.index

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.shopify.testify.sample.R
import com.shopify.testify.sample.clients.MockClientData
import com.shopify.testify.sample.clients.details.ClientDetailsActivity.Companion.startClientDetailsActivity
import kotlinx.android.synthetic.main.activity_client_list.fab
import kotlinx.android.synthetic.main.activity_client_list.root_view as rootView

class ClientListActivity : AppCompatActivity(), ClientListFragment.OnListFragmentInteractionListener {

    override fun onListFragmentInteraction(item: MockClientData.Client) {
        this.startClientDetailsActivity(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_list)

        supportActionBar?.title = getString(R.string.client_list_title)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "TODO: Add a client", Snackbar.LENGTH_LONG).show()
        }
    }
}
