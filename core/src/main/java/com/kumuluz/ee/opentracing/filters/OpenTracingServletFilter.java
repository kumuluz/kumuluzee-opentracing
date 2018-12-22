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

import com.kumuluz.ee.opentracing.utils.CommonUtil;
import com.kumuluz.ee.opentracing.utils.SpanErrorLogger;
import io.opentracing.Span;
import io.opentracing.tag.Tags;

import javax.enterprise.context.ApplicationScoped;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Open Tracing Servlet filter
 * @author Domen Jeric
 * @since 1.0.0
 */
@ApplicationScoped
@WebFilter(filterName = "OpenTracingServletFilter", urlPatterns = {"/*"}, asyncSupported = true)
public class OpenTracingServletFilter implements Filter {

    @Override
    public void init(FilterConfig var1)  throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            Span span = (Span) request.getAttribute(CommonUtil.OPENTRACING_SPAN_TITLE);

            if (span == null) {
                return;
            }

            HttpServletResponse httpResponse = (HttpServletResponse) response;

            span.setTag(Tags.HTTP_STATUS.getKey(), httpResponse.getStatus());

            if (httpResponse.getStatus() >= 500) {
                SpanErrorLogger.addExceptionLogs(span, e);
            }

            span.finish();
        }
    }

    @Override
    public void destroy() {

    }

}
