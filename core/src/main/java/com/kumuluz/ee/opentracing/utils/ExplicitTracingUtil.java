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

import org.eclipse.microprofile.opentracing.Traced;

import javax.interceptor.InvocationContext;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.UriInfo;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * Explicit tracing util
 * @author Domen Jeric
 * @since 1.0.0
 */
public class ExplicitTracingUtil {

    public static boolean tracingDisabled(ResourceInfo resourceInfo) {
        Traced tracedAnnotation = getAnnotation(resourceInfo.getResourceClass(), resourceInfo.getResourceMethod());
        return tracedAnnotation != null && !tracedAnnotation.value();
    }

    public static boolean tracingDisabled(InvocationContext context) {
        Traced tracedAnnotation = getAnnotation(context);
        return tracedAnnotation != null && !tracedAnnotation.value();
    }

    public static Traced getAnnotation(InvocationContext context) {
        Class<?> clazz = context.getTarget().getClass().getSuperclass();
        return getAnnotation(clazz, context.getMethod());
    }

    public static Traced getAnnotation(Class<?> clazz, Method method) {
        return method.isAnnotationPresent(Traced.class) ?
                method.getAnnotation(Traced.class) :
                clazz.getAnnotation(Traced.class);
    }

    public static boolean pathMatchesSkipPattern(UriInfo uriInfo, Pattern skipPattern) {
        String path = uriInfo.getPath();

        if (path.charAt(0) != '/') {
            path = "/" + path;
        }

        return skipPattern.matcher(path).matches();
    }
}
