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

package com.kumuluz.ee.opentracing.jaeger.config;


import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Jaeger config class
 *
 * @author Domen Kajdic
 * @author Domen Jeric
 * @since 1.0.0
 */
@ApplicationScoped
public class JaegerConfig {

    private static final String JAEGER_CONFIG = "kumuluzee.opentracing.jaeger.";

    @Inject
    @ConfigProperty(name = JAEGER_CONFIG + "service-name")
    private Optional<String> serviceName;

    @Inject
    @ConfigProperty(name = JAEGER_CONFIG + "agent-host")
    private Optional<String> agentHost;

    @Inject
    @ConfigProperty(name = JAEGER_CONFIG + "agent-port")
    private Optional<Integer> agentPort;

    @Inject
    @ConfigProperty(name = JAEGER_CONFIG + "endpoint")
    private Optional<String> endpoint;

    @Inject
    @ConfigProperty(name = JAEGER_CONFIG + "auth-token")
    private Optional<String> authToken;

    @Inject
    @ConfigProperty(name = JAEGER_CONFIG + "username")
    private Optional<String> username;

    @Inject
    @ConfigProperty(name = JAEGER_CONFIG + "password")
    private Optional<String> password;

    @Inject
    @ConfigProperty(name = JAEGER_CONFIG + "reporter.log-spans")
    private Optional<Boolean> logSpans;

    @Inject
    @ConfigProperty(name = JAEGER_CONFIG + "reporter.max-queue-size")
    private Optional<Integer> maxQueueSize;

    @Inject
    @ConfigProperty(name = JAEGER_CONFIG + "reporter.flush-interval")
    private Optional<Integer> flushInterval;

    @Inject
    @ConfigProperty(name = JAEGER_CONFIG + "tags")
    private Optional<String> tags;

    @Inject
    @ConfigProperty(name = JAEGER_CONFIG + "sampler.type")
    private Optional<String> samplerType;

    @Inject
    @ConfigProperty(name = JAEGER_CONFIG + "sampler.param")
    private Optional<Integer> sampleParam;

    @Inject
    @ConfigProperty(name = JAEGER_CONFIG + "sampler.manager-host-port")
    private Optional<String> samplerHostPort;

    @Inject
    @ConfigProperty(name = JAEGER_CONFIG + "propagation")
    private Optional<String> propagation;

    @Inject
    @ConfigProperty(name = JAEGER_CONFIG + "traceid-128bit")
    private Optional<Boolean> useTraceId128Bit;


    public String getServiceName() {
        return serviceName.orElse(null);
    }

    public String getAgentHost() {
        return agentHost.orElse(null);
    }

    public Integer getAgentPort() {
        return agentPort.orElse(null);
    }

    public String getEndpoint() {
        return endpoint.orElse(null);
    }

    public String getAuthToken() {
        return authToken.orElse(null);
    }

    public String getUsername() {
        return username.orElse(null);
    }

    public String getPassword() {
        return password.orElse(null);
    }

    public Boolean getLogSpans() {
        return logSpans.orElse(null);
    }

    public Integer getMaxQueueSize() {
        return maxQueueSize.orElse(null);
    }

    public Integer getFlushInterval() {
        return flushInterval.orElse(null);
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
        return samplerHostPort.orElse(null);
    }

    public String getPropagation() {
        return propagation.orElse(null);
    }

    public Boolean getUseTraceId128Bit() {
        return useTraceId128Bit.orElse(null);
    }
}
