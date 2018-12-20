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
package com.kumuluz.ee.opentracing;

import com.kumuluz.ee.opentracing.providers.ClientTracingProvider;
import org.jboss.arquillian.container.test.spi.client.deployment.CachedAuxilliaryArchiveAppender;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

/**
 * Packages KumuluzEE OpenTracing library as a ShrinkWrap archive and adds it to deployments.
 *
 * @author Urban Malc
 * @author Domen Jeric
 * @since 1.0.0
 */
public class OpenTracingLibraryAppender extends CachedAuxilliaryArchiveAppender {

    @Override
    protected Archive<?> buildArchive() {

        return ShrinkWrap.create(JavaArchive.class, "kumuluzee-opentracing.jar")
                .addPackages(true, OpenTracingExtension.class.getPackage())
                .addClass(MockTracerInitializer.class)
                .addAsServiceProvider(com.kumuluz.ee.common.Extension.class, OpenTracingExtension.class)
                .addAsServiceProvider(org.eclipse.microprofile.opentracing.ClientTracingRegistrarProvider.class, ClientTracingProvider.class)
                .addAsResource("META-INF/beans.xml");
    }
}
