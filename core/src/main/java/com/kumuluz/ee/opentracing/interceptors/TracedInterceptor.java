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

package com.kumuluz.ee.opentracing.interceptors;

import com.kumuluz.ee.opentracing.config.OpenTracingConfig;
import com.kumuluz.ee.opentracing.utils.ExplicitTracingUtil;
import com.kumuluz.ee.opentracing.utils.OperationNameUtil;
import com.kumuluz.ee.opentracing.utils.RequestContextHolder;
import com.kumuluz.ee.opentracing.utils.SpanErrorLogger;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.eclipse.microprofile.opentracing.Traced;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.ws.rs.container.ContainerRequestContext;
import java.util.regex.Pattern;

/**
 * MicroProfile Traced annotation implementation
 * @author Domen Jeric
 * @since 1.0.0
 */
@Interceptor
@Traced
@Priority(Interceptor.Priority.APPLICATION)
public class TracedInterceptor {

    @Inject
    private OpenTracingConfig tracerConfig;

    @Inject
    private OperationNameUtil operationNameUtil;

    @Inject
    private Tracer tracer;

    @AroundInvoke
    public Object trace(InvocationContext context) throws Exception {
        Pattern skipPattern = tracerConfig.getSkipPattern();
        ContainerRequestContext requestContext = RequestContextHolder.getRequestContext();

        if (ExplicitTracingUtil.tracingDisabled(context) ||
            requestContext != null && ExplicitTracingUtil.pathMatchesSkipPattern(requestContext.getUriInfo(), skipPattern) ||
            ExplicitTracingUtil.isJaxRsResourceMethod(context.getMethod())) {
            return context.proceed();
        }

        Span parentSpan = tracer.activeSpan();
        String operationName = operationNameUtil.operationNameExplicitTracing(requestContext, context);
        Span childSpan = tracer.buildSpan(operationName).asChildOf(parentSpan).start();

        try (Scope ignored = tracer.activateSpan(childSpan)) {
            try {
                return context.proceed();
            } catch (Exception e) {
                SpanErrorLogger.addExceptionLogs(childSpan, e);
                throw e;
            } finally {
                childSpan.finish();
            }
        }
    }
}
