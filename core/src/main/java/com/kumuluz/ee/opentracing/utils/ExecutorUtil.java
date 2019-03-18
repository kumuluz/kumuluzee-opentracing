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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Singleton providing a thread pool for traced executor services.
 *
 * @author Urban Malc
 * @since 1.3.0
 */
public class ExecutorUtil {

    private static ExecutorUtil instance = null;

    private ExecutorService executorService;

    private ExecutorUtil() {
        executorService = Executors.newFixedThreadPool(10);
    }

    public static ExecutorUtil getInstance() {
        if (instance == null) {
            initialize();
        }

        return instance;
    }

    private static synchronized void initialize() {
        if (instance == null) {
            instance = new ExecutorUtil();
        }
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }
}
