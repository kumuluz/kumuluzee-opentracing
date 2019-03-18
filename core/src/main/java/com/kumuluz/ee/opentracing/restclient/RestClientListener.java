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
package com.kumuluz.ee.opentracing.restclient;

import com.kumuluz.ee.opentracing.filters.OpenTracingClientRequestFilter;
import com.kumuluz.ee.opentracing.filters.OpenTracingClientResponseFilter;
import com.kumuluz.ee.opentracing.utils.ExecutorUtil;
import io.opentracing.Tracer;
import io.opentracing.contrib.concurrent.TracedExecutorService;
import org.eclipse.microprofile.opentracing.Traced;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

import javax.enterprise.inject.spi.CDI;

/**
 * Configures MicroProfile Rest Client with tracing support.
 *
 * @author Urban Malc
 * @since 1.3.0
 */
public class RestClientListener implements org.eclipse.microprofile.rest.client.spi.RestClientListener {

    @Override
    public void onNewClient(Class<?> serviceInterface, RestClientBuilder builder) {
        Traced traced = serviceInterface.getAnnotation(Traced.class);

        Tracer tracer = CDI.current().select(Tracer.class).get();

        if (traced == null || traced.value()) {
            builder.register(new OpenTracingClientRequestFilter(tracer));
            builder.register(OpenTracingClientResponseFilter.class);
            builder.executorService(new TracedExecutorService(ExecutorUtil.getInstance().getExecutorService(), tracer));
        }
    }
}
