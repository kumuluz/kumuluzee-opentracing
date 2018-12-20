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

package com.kumuluz.ee.opentracing.filters;

import com.kumuluz.ee.opentracing.adapters.ClientHeaderInjectAdapter;
import com.kumuluz.ee.opentracing.utils.OpenTracingUtil;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import io.opentracing.tag.Tags;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.ext.Provider;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Jax-rs Client request filter
 * @author Domen Jeric
 * @since 1.0.0
 */
@ApplicationScoped
@Provider
public class OpenTracingClientRequestFilter implements ClientRequestFilter {

    private static final Logger LOG = Logger.getLogger(OpenTracingClientRequestFilter.class.getName());

    @Inject
    Tracer tracer;

    @Override
    public void filter(ClientRequestContext requestContext) {
        try {
            URI uri = requestContext.getUri();

            Tracer.SpanBuilder spanBuilder = tracer.buildSpan(requestContext.getMethod())
                    .asChildOf(tracer.activeSpan())
                    .withTag(Tags.SPAN_KIND.getKey(), Tags.SPAN_KIND_CLIENT)
                    .withTag(Tags.HTTP_METHOD.getKey(), requestContext.getMethod())
                    .withTag(Tags.HTTP_URL.getKey(),  uri != null ? uri.toURL().toString() : "")
                    .withTag(Tags.COMPONENT.getKey(), "jaxrs")
                    .withTag(Tags.PEER_PORT.getKey(), uri != null ? uri.getPort() : 0)
                    .withTag(Tags.PEER_HOSTNAME.getKey(), uri != null ? uri.getHost() : "");

            Span span = spanBuilder.start();

            tracer.inject(span.context(), Format.Builtin.HTTP_HEADERS, new ClientHeaderInjectAdapter(requestContext.getHeaders()));

            requestContext.setProperty(OpenTracingUtil.OPENTRACING_SPAN_TITLE, span);

        } catch(Exception exception) {
            LOG.log(Level.SEVERE,"Exception occured when trying to start client span.", exception);
        }
    }
}
