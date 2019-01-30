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

package com.kumuluz.ee.opentracing.providers;

import com.kumuluz.ee.opentracing.filters.OpenTracingClientRequestFilter;
import com.kumuluz.ee.opentracing.filters.OpenTracingClientResponseFilter;
import io.opentracing.Tracer;
import io.opentracing.contrib.concurrent.TracedExecutorService;
import org.eclipse.microprofile.opentracing.ClientTracingRegistrarProvider;

import javax.enterprise.inject.spi.CDI;
import javax.ws.rs.client.ClientBuilder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Client tracing provider
 * @author Domen Kajdic
 * @since 1.0.0
 */
public class ClientTracingProvider implements ClientTracingRegistrarProvider {
    @Override
    public ClientBuilder configure(ClientBuilder clientBuilder) {
        return configure(clientBuilder, Executors.newFixedThreadPool(10));
    }

    @Override
    public ClientBuilder configure(ClientBuilder clientBuilder, ExecutorService executorService) {
        Tracer tracer = CDI.current().select(Tracer.class).get();
        return clientBuilder
                .register(new OpenTracingClientRequestFilter(tracer))
                .register(new OpenTracingClientResponseFilter())
                .executorService(new TracedExecutorService(executorService, tracer));
    }
}
