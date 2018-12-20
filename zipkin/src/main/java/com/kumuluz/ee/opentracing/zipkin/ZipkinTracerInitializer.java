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

package com.kumuluz.ee.opentracing.zipkin;

import com.kumuluz.ee.opentracing.utils.CommonUtils;
import com.kumuluz.ee.opentracing.zipkin.config.ZipkinConfig;
import io.jaegertracing.Configuration;
import io.jaegertracing.spi.Reporter;
import io.jaegertracing.zipkin.ZipkinV2Reporter;
import io.opentracing.Tracer;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.urlconnection.URLConnectionSender;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Zipkin tracer initializer class
 *
 * @author Domen Kajdic
 * @since 1.0.0
 */
@ApplicationScoped
public class ZipkinTracerInitializer {
    private static final Logger LOG = Logger.getLogger(ZipkinTracerInitializer.class.getName());

    @Inject
    private ZipkinConfig config;

    private void initialise(@Observes @Initialized(ApplicationScoped.class) Object init) {
        LOG.info("Initializing OpenTracing extension with Zipkin tracking.");
        if (init instanceof ServletContext) {
            ServletContext servletContext = (ServletContext) init;

            String serviceName = CommonUtils.getServiceName(config.getServiceName());
            Map<String, String> tags = CommonUtils.getTagsFromTagString(config.getTags());

            String zipkinHost = config.getZipkinHost();
            if(zipkinHost == null || zipkinHost.isEmpty()) {
                zipkinHost = "http://localhost:9411";
            }

            Reporter reporter = new ZipkinV2Reporter(AsyncReporter.create(URLConnectionSender.create(zipkinHost + "/api/v2/spans")));

            Configuration.SamplerConfiguration samplerConfiguration = new Configuration.SamplerConfiguration()
                    .withManagerHostPort(config.getSamplerHostPort())
                    .withParam(config.getSampleParam())
                    .withType(config.getSamplerType());

            Configuration.CodecConfiguration codecConfiguration = new Configuration.CodecConfiguration();
            codecConfiguration.withPropagation(Configuration.Propagation.B3);

            Configuration configuration = new Configuration(serviceName)
                    .withSampler(samplerConfiguration)
                    .withCodec(codecConfiguration)
                    .withTracerTags(tags);

            Boolean useTraceId128Bit = config.getUseTraceId128Bit();
            if(useTraceId128Bit != null) {
                configuration.withTraceId128Bit(useTraceId128Bit);
            }

            Tracer tracer = configuration
                    .getTracerBuilder()
                    .withReporter(reporter)
                    .build();

            servletContext.setAttribute("tracer", tracer);

            LOG.info("OpenTracing extension sucesfully initialized.");
        } else {
            LOG.warning("Failed while initializing Zipkin tracing.");
        }
    }
}
