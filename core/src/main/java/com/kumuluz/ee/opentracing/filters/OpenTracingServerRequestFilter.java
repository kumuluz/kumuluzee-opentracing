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


import com.kumuluz.ee.opentracing.adapters.ServerHeaderExtractAdapter;
import com.kumuluz.ee.opentracing.config.OpenTracingConfig;
import com.kumuluz.ee.opentracing.utils.ExplicitTracingUtil;
import com.kumuluz.ee.opentracing.utils.OpenTracingUtil;
import com.kumuluz.ee.opentracing.utils.OperationNameUtil;
import com.kumuluz.ee.opentracing.utils.RequestContextHolder;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import io.opentracing.tag.Tags;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Jax-rs server request filter
 * @author Domen Jeric
 * @since 1.0.0
 */
@ApplicationScoped
@Provider
public class OpenTracingServerRequestFilter implements ContainerRequestFilter {

    @Context
    ResourceInfo resourceInfo;

    @Inject
    OperationNameUtil operationNameUtil;

    @Inject
    OpenTracingConfig tracerConfig;

    @Inject
    Tracer tracer;

    private static final Logger LOG = Logger.getLogger(OpenTracingServerRequestFilter.class.getName());

    public void filter(ContainerRequestContext requestContext) {

        RequestContextHolder.setRequestContextHolder(requestContext);

        Pattern skipPattern = tracerConfig.getSkipPattern();

        if (skipPattern != null && ExplicitTracingUtil.pathMatchesSkipPattern(requestContext.getUriInfo(), skipPattern)
                || ExplicitTracingUtil.tracingDisabled(resourceInfo)) {
            return;
        }

        String operationName = operationNameUtil.operationName(requestContext, resourceInfo);

        Tracer.SpanBuilder spanBuilder;

        try {

            SpanContext parentSpan = tracer.extract(Format.Builtin.HTTP_HEADERS,
                    new ServerHeaderExtractAdapter(requestContext.getHeaders()));
            spanBuilder = tracer.buildSpan(operationName);

            if (parentSpan != null) {
                spanBuilder = spanBuilder.asChildOf(parentSpan);
            }

            spanBuilder = spanBuilder
                    .withTag(Tags.SPAN_KIND.getKey(), Tags.SPAN_KIND_SERVER)
                    .withTag(Tags.HTTP_METHOD.getKey(), requestContext.getMethod())
                    .withTag(Tags.HTTP_URL.getKey(),
                            requestContext.getUriInfo().getBaseUri().toString() + requestContext.getUriInfo().getPath())
                    .withTag(Tags.COMPONENT.getKey(), "jaxrs");

            requestContext.setProperty(OpenTracingUtil.OPENTRACING_SPAN_TITLE,
                    spanBuilder.startActive(true).span());

        } catch(Exception exception) {
            LOG.log(Level.SEVERE,"Exception occured when trying to start server span.", exception);
        }
    }

}
