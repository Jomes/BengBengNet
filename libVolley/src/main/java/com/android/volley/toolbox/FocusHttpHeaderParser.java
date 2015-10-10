/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.volley.toolbox;

import java.util.Map;

import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;

/**
 *仅处理焦点的请求 服务器没有返回有意义的header
 *
 *需要完全利用本地是来判断是否过期
 *
 *尽量做到不删除或者修改volley的原生代码,可以增加
 */
public class FocusHttpHeaderParser {

    /**
     * Extracts a {@link Cache.Entry} from a {@link NetworkResponse}.
     *
     * @param response The network response to parse headers from
     * @return a cache entry for the given response, or null if the response is not cacheable.
     */
    public static Cache.Entry parseCacheHeaders(NetworkResponse response) {
        Map<String, String> headers = response.headers;
        long serverDate = 0;
        long localDate = System.currentTimeMillis();
        if( response.localDate != 0)
            localDate = response.localDate;
        long softExpire = localDate*2;;
        String headerValue;
        headerValue = headers.get("Date");
        if (headerValue != null) {
            serverDate = parseDateAsEpoch(headerValue);
        }

        Cache.Entry entry = new Cache.Entry();
        entry.data = response.data;
        entry.softTtl = softExpire;
        entry.ttl = softExpire;
        entry.serverDate = serverDate;
        entry.localDate = localDate; //利用本地时间来判断是否过期 //by lbritney
        entry.responseHeaders = headers;

        return entry;
    }

    /**
     * Parse date in RFC1123 format, and return its value as epoch
     */
    public static long parseDateAsEpoch(String dateStr) {
        try {
            // Parse date in RFC1123 format if this header contains one
            return DateUtils.parseDate(dateStr).getTime();
        } catch (DateParseException e) {
            // Date in invalid format, fallback to 0
            return 0;
        }
    }
}
