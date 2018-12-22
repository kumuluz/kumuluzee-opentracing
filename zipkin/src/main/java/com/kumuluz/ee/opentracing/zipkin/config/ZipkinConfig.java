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

package com.kumuluz.ee.opentracing.zipkin.config;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Zipkin config class
 *
 * @author Domen Kajdic
 * @author Domen Jeric
 * @since 1.0.0
 */
@ApplicationScoped
public class ZipkinConfig {

    private static final String ZIPKIN_CONFIG = "kumuluzee.opentracing.zipkin.";

    @Inject
    @ConfigProperty(name = ZIPKIN_CONFIG + "service-name")
    private Optional<String> serviceName;

    @Inject
    @ConfigProperty(name = ZIPKIN_CONFIG + "agent-host")
    private Optional<String> zipkinHost;

    @Inject
    @ConfigProperty(name = ZIPKIN_CONFIG + "tags")
    private Optional<String> tags;

    @Inject
    @ConfigProperty(name = ZIPKIN_CONFIG + "sampler.type")
    private Optional<String> samplerType;

    @Inject
    @ConfigProperty(name = ZIPKIN_CONFIG + "sampler.param")
    private Optional<Integer> sampleParam;

    @Inject
    @ConfigProperty(name = ZIPKIN_CONFIG + "agent-port")
    private Optional<String> samplerHostPort;

    @Inject
    @ConfigProperty(name = ZIPKIN_CONFIG + "traceid-128bit")
    private Optional<Boolean> useTraceId128Bit;


    public String getServiceName() {
        return serviceName.orElse(null);
    }

    public String getZipkinHost() {
        return zipkinHost.orElse("http://localhost");
    }

    public String getTags() {
        return tags.orElse(null);
    }

    public String getSamplerType() {
        return samplerType.orElse("const");
    }

    public Integer getSampleParam() {
        return sampleParam.orElse(1);
    }

    public String getSamplerHostPort() {
        return samplerHostPort.orElse("9411");
    }

    public Boolean getUseTraceId128Bit() {
        return useTraceId128Bit.orElse(null);
    }
}
