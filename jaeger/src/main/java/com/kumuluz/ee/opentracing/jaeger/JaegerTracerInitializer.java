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

package com.kumuluz.ee.opentracing.jaeger;

import com.kumuluz.ee.opentracing.config.OpenTracingConfig;
import com.kumuluz.ee.opentracing.jaeger.config.JaegerConfig;
import com.kumuluz.ee.opentracing.utils.CommonUtil;
import io.jaegertracing.Configuration;
import io.opentracing.Tracer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Jaeger tracer initializer class
 *
 * @author Domen Kajdic
 * @since 1.0.0
 */
@ApplicationScoped
public class JaegerTracerInitializer {
    private static final Logger LOG = Logger.getLogger(JaegerTracerInitializer.class.getName());

    @Inject
    private JaegerConfig jaegerConfig;

    @Inject
    private OpenTracingConfig openTracingConfig;

    private void initialise(@Observes @Initialized(ApplicationScoped.class) Object init) {
        LOG.info("Initializing OpenTracing extension with Jaeger tracing.");

        if (init instanceof ServletContext) {
            ServletContext servletContext = (ServletContext) init;

            String serviceName = openTracingConfig.getServiceName(jaegerConfig.getServiceName());
            Map<String, String> tags = CommonUtil.getTagsFromTagString(jaegerConfig.getTags());

            Configuration.SenderConfiguration senderConfiguration = new Configuration.SenderConfiguration()
                    .withAgentHost(jaegerConfig.getAgentHost())
                    .withAgentPort(jaegerConfig.getAgentPort())
                    .withEndpoint(jaegerConfig.getEndpoint())
                    .withAuthToken(jaegerConfig.getAuthToken())
                    .withAuthUsername(jaegerConfig.getUsername())
                    .withAuthPassword(jaegerConfig.getPassword());

            Configuration.ReporterConfiguration reporterConfiguration = new Configuration.ReporterConfiguration()
                    .withSender(senderConfiguration)
                    .withLogSpans(jaegerConfig.getLogSpans())
                    .withMaxQueueSize(jaegerConfig.getMaxQueueSize())
                    .withFlushInterval(jaegerConfig.getFlushInterval());

            Configuration.SamplerConfiguration samplerConfiguration = new Configuration.SamplerConfiguration()
                    .withManagerHostPort(jaegerConfig.getSamplerHostPort())
                    .withParam(jaegerConfig.getSampleParam())
                    .withType(jaegerConfig.getSamplerType());

            Configuration.CodecConfiguration codecConfiguration = new Configuration.CodecConfiguration();
            String propagation = jaegerConfig.getPropagation();

            if(propagation != null && !propagation.isEmpty()) {
                codecConfiguration.withPropagation(Configuration.Propagation.valueOf(propagation));
            }

            Configuration configuration = new Configuration(serviceName)
                    .withReporter(reporterConfiguration)
                    .withSampler(samplerConfiguration)
                    .withCodec(codecConfiguration)
                    .withTracerTags(tags);

            Boolean useTraceId128Bit = jaegerConfig.getUseTraceId128Bit();

            if(useTraceId128Bit != null) {
                configuration.withTraceId128Bit(useTraceId128Bit);
            }

            Tracer tracer = configuration.getTracer();
            servletContext.setAttribute("tracer", tracer);

            LOG.info("OpenTracing extension successfully initialized.");
        } else {
            LOG.warning("Failed initializing Jaeger tracing.");
        }
    }
}
