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


import org.eclipse.microprofile.opentracing.tck.application.TestWebServicesApplication;
import org.eclipse.microprofile.opentracing.tck.rest.client.RestClientApplication;
import org.jboss.arquillian.container.test.spi.client.deployment.ApplicationArchiveProcessor;
import org.jboss.arquillian.test.spi.TestClass;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;

/**
 * Adds appropriate web.xml to deployments. TCK apps do not have provider discovery enabled, so we explicitly register
 * required providers in web.xml.
 *
 * @author Domen Jeric
 * @since 1.0.0
 */
public class OpenTracingArchiveProcessor implements ApplicationArchiveProcessor {

    @Override
    public void process(Archive<?> archive, TestClass testClass) {
        WebArchive war = archive.as(WebArchive.class);
        if (isNonRestClientDeployment(war)) {
            war.deleteClass(RestClientApplication.class);
            war.addAsWebInfResource("WEB-INF/web.xml");
        } else {
            war.addAsWebInfResource("WEB-INF/web-rc.xml", "web.xml");
        }
    }

    private boolean isNonRestClientDeployment(WebArchive war) {
        return war.contains("/WEB-INF/classes/" +
                TestWebServicesApplication.class.getName().replace(".", "/") +
                ".class");
    }
}
