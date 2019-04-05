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
package com.shopify.testify.sample.clients

import java.io.Serializable

object MockClientData {

    data class Client(val id: Int, val name: String, val date: String, val avatar: String) : Serializable

    val CLIENTS = listOf(
        Client(id = 1, name = "Daniel Jetté", date = "Mar 28, 1987", avatar = "avatar1"),
        Client(id = 2, name = "Lyndsey Ware", date = "Mar 24, 1993", avatar = "avatar2"),
        Client(id = 3, name = "Rhona Dejesus", date = "Jun 10, 1981", avatar = "avatar3"),
        Client(id = 4, name = "Cem Cain", date = "Aug 15, 2015", avatar = "avatar1"),
        Client(id = 5, name = "Junayd Escobar", date = "Sep 1, 2001", avatar = "avatar3"),
        Client(id = 6, name = "Ayla Ruiz", date = "Nov 5, 2006", avatar = "avatar3"),
        Client(id = 7, name = "Allan Bostock", date = "Jan 22, 1987", avatar = "avatar1"),
        Client(id = 8, name = "Bryn Cruz", date = "Mar 10, 1984", avatar = "avatar2"),
        Client(id = 9, name = "Tobey Blake", date = "Oct 22, 1983", avatar = "avatar2"),
        Client(id = 10, name = "Alba Hendricks", date = "Nov 3, 1992", avatar = "avatar3"),
        Client(id = 11, name = "Kyla Little", date = "Dec 25, 1985", avatar = "avatar3"),
        Client(id = 12, name = "Ho Gilbert", date = "Jan 15, 2012", avatar = "avatar2"),
        Client(id = 13, name = "Elsa Hough", date = "May 3, 2004", avatar = "avatar1"),
        Client(id = 14, name = "Hanan Gregory", date = "May 25, 1986", avatar = "avatar1"),
        Client(id = 15, name = "Jim Moore", date = "Jun 27, 2012", avatar = "avatar2"),
        Client(id = 16, name = "Frankie Larsen", date = "Feb 18, 1989", avatar = "avatar3"),
        Client(id = 17, name = "Karishma Hunt", date = "Aug 5, 2006", avatar = "avatar2"),
        Client(id = 18, name = "Steve Hutton", date = "Jul 10, 1998", avatar = "avatar1"),
        Client(id = 19, name = "Jaidan Schneider", date = "Mar 28, 1981", avatar = "avatar3"),
        Client(id = 20, name = "Denis Davie", date = "Jan 2, 1982", avatar = "avatar2")
    )
}
