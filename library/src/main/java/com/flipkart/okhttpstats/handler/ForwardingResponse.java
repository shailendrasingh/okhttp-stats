/*
 * The MIT License
 *
 * Copyright 2016 Flipkart Internet Pvt. Ltd.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom
 * the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.flipkart.okhttpstats.handler;

import android.net.NetworkInfo;

import com.flipkart.okhttpstats.model.RequestStats;
import com.flipkart.okhttpstats.toolbox.HttpStatusCode;

public class ForwardingResponse implements OnResponseListener {

    private OnStatusCodeAwareResponseListener mOnStatusCodeAwareResponseListener;


    public ForwardingResponse(OnStatusCodeAwareResponseListener onStatusCodeAwareResponseListener) {
        mOnStatusCodeAwareResponseListener = onStatusCodeAwareResponseListener;
    }

    @Override
    public void onResponseSuccess(NetworkInfo info, RequestStats requestStats) {
        if (requestStats != null) {
            int statusCode = requestStats.statusCode;
            if ((statusCode >= HttpStatusCode.HTTP_2XX_START && statusCode <= HttpStatusCode.HTTP_2XX_END) ||
                    (statusCode >= HttpStatusCode.HTTP_3XX_START && statusCode <= HttpStatusCode.HTTP_3XX_END)) {
                mOnStatusCodeAwareResponseListener.onResponseServerSuccess(info, requestStats);
            } else if ((statusCode >= HttpStatusCode.HTTP_4XX_START && statusCode <= HttpStatusCode.HTTP_4XX_END) ||
                    (statusCode >= HttpStatusCode.HTTP_5XX_START && statusCode <= HttpStatusCode.HTTP_5XX_END)) {
                mOnStatusCodeAwareResponseListener.onResponseServerError(info, requestStats);
            }
        }
    }

    @Override
    public void onResponseError(NetworkInfo info, RequestStats requestStats, Exception e) {
        mOnStatusCodeAwareResponseListener.onResponseNetworkError(info, requestStats, e);
    }
}