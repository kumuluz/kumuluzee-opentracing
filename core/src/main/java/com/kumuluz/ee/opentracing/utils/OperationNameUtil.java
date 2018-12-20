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

import com.kumuluz.ee.opentracing.config.OpenTracingConfig;
import org.eclipse.microprofile.opentracing.Traced;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.interceptor.InvocationContext;
import javax.ws.rs.Path;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;
import java.lang.reflect.Method;

/**
 * Server Span name util
 * @author Domen Jeric
 * @since 1.0.0
 */
@ApplicationScoped
public class OperationNameUtil {

    @Inject
    OpenTracingConfig tracerConfig;

    public String operationName(ContainerRequestContext requestContext, ResourceInfo resourceInfo) {
        return this.operationName(requestContext, resourceInfo.getResourceClass(), resourceInfo.getResourceMethod());
    }

    public String operationNameExplicitTracing(ContainerRequestContext requestContext, InvocationContext context) {
        Class<?> clazz = context.getTarget().getClass().getSuperclass();
        Method method = context.getMethod();

        Traced tracedAnnotation = ExplicitTracingUtil.getAnnotation(context);

        if (tracedAnnotation != null && !tracedAnnotation.operationName().equals("")) {
            return tracedAnnotation.operationName();
        }

        if (clazz.isAnnotationPresent(Path.class) || method.isAnnotationPresent(Path.class)) {
            return this.operationName(requestContext, clazz, method);
        }

        return clazz.getName() + "." + method.getName();
    }

    private String operationName(ContainerRequestContext requestContext, Class<?> clazz, Method method) {
        String operationNameProvider = this.operationNameProvider();

        if (operationNameProvider != null && operationNameProvider.equals("http-path")) {
            return this.operationNameHttpPath(requestContext);
        }
        return this.operationNameClassMethod(requestContext, clazz, method);
    }

    private String operationNameProvider() {
        return tracerConfig.getSelectedOperationNameProvider();
    }

    private String operationNameClassMethod(ContainerRequestContext requestContext, Class<?> clazz, Method method) {
        return requestContext.getMethod() + ":" + clazz.getName() + "." + method.getName();
    }


    private String operationNameHttpPath(ContainerRequestContext requestContext) {
        return requestContext.getMethod() + ":/" + requestContext.getUriInfo().getPath();
    }

}
