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

package com.kumuluz.ee.opentracing.config;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;

import javax.enterprise.context.ApplicationScoped;
import java.util.regex.Pattern;

/**
 * OpenTracing config
 * @author Domen Jeric
 * @since 1.0.0
 */
@ApplicationScoped
public class OpenTracingConfig {

    private static final String DEFAULT_SERVICE_NAME = "KumuluzEE Project";
    private static final String MP_CONFIG_PREFIX = "mp.opentracing.";

    public String getServiceName() {
        return ConfigurationUtil.getInstance()
                .get("kumuluzee.name")
                .orElse(DEFAULT_SERVICE_NAME);
    }

    public String getSelectedOperationNameProvider() {
        return ConfigurationUtil.getInstance()
                .get(MP_CONFIG_PREFIX + "server.operation-name-provider")
                .orElse("class-method");
    }

    public Pattern getSkipPattern() {
        String skipPattern = ConfigurationUtil.getInstance()
                .get(MP_CONFIG_PREFIX + "server.skip-pattern")
                .orElse(null);

        return skipPattern != null ? Pattern.compile(skipPattern) : null;
    }
}
