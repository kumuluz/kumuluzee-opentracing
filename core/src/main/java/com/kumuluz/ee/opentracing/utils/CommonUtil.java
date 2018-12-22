/*
 * Copyright (c) 2018 Sunesis, Ltd. and/or its affiliates
 * and other contributors as indicated by the @author tags and
 * the contributor list.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.kumuluz.ee.opentracing.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Common utils
 * @author Domen Kajdic
 * @since 1.0.0
 */
public class CommonUtil {
    private static final Logger LOG = Logger.getLogger(CommonUtil.class.getName());

    public static final String OPENTRACING_SPAN_TITLE = "opentracing-span";

    public static Map<String, String> getTagsFromTagString(String tagString) {
        Map<String, String> tags = new HashMap<>();
        if(tagString != null && !tagString.isEmpty()) {
            try {
                String[] arrayOfTags = tagString.split(",");
                for(String tag: arrayOfTags) {
                    String[] split = tag.split("=");
                    tags.put(split[0], split[1]);
                }
            } catch (Exception e) {
                LOG.warning("Invalid tag format provided. Format of tags should be like tag1=value1,tag2=value2.");
            }
        }
        return tags;
    }
}
