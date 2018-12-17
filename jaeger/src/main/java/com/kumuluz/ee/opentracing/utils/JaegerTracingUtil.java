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

import com.kumuluz.ee.opentracing.config.JaegerTracingConfig;
import com.kumuluz.ee.opentracing.config.OpenTracingConfig;
import io.jaegertracing.Configuration;
import io.jaegertracing.internal.samplers.ConstSampler;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JaegerTracing util class
 * @author Domen Jeric
 * @since 1.0.0
 */
@ApplicationScoped
public class JaegerTracingUtil implements OpenTracingUtil {

    @Inject
    JaegerTracingConfig jaegerConfig;

    @Inject
    OpenTracingConfig tracingConfig;

    private static final Logger LOG = Logger.getLogger(JaegerTracingUtil.class.getName());

    @Override
    public void init() {

        try {

            Configuration.SamplerConfiguration samplerConfig = new Configuration.SamplerConfiguration()
                    .withType(ConstSampler.TYPE)
                    .withParam(1);

            Configuration.SenderConfiguration senderConfig = new Configuration.SenderConfiguration()
                    .withAgentHost(jaegerConfig.getReporterHost())
                    .withAgentPort(jaegerConfig.getReporterPort());

            Configuration.ReporterConfiguration reporterConfig = new Configuration.ReporterConfiguration()
                    .withLogSpans(true)
                    .withFlushInterval(1000)
                    .withMaxQueueSize(10000)
                    .withSender(senderConfig);

            Tracer tracer = new Configuration(tracingConfig.getServiceName()).withSampler(samplerConfig).withReporter(reporterConfig).getTracer();
            GlobalTracer.register(tracer);

        } catch(Exception exception) {
            LOG.log(Level.SEVERE,"Exception occured when trying to initialize JaegerTracer.", exception);
        }

    }

}
