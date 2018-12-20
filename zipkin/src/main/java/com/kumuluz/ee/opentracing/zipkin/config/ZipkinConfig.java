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

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;

/**
 * Zipkin config class
 *
 * @author Domen Kajdic
 * @since 1.0.0
 */
@ApplicationScoped
@ConfigBundle("kumuluzee.opentracing.jaeger")
public class ZipkinConfig {
    @ConfigValue("service-name")
    private String serviceName;

    @ConfigValue("zipkin-host")
    private String zipkinHost;

    @ConfigValue("tags")
    private String tags;

    @ConfigValue("sampler.type")
    private String samplerType;

    @ConfigValue("sampler.param")
    private Integer sampleParam;

    @ConfigValue("sampler.manager-host-port")
    private String samplerHostPort;

    @ConfigValue("traceid-128bit")
    private Boolean useTraceId128Bit;


    public String getServiceName() {
        return serviceName;
    }

    public String getZipkinHost() {
        return zipkinHost;
    }

    public String getTags() {
        return tags;
    }

    public String getSamplerType() {
        if(samplerType == null || samplerType.isEmpty()) {
            return "const";
        } else {
            return samplerType;
        }
    }

    public Integer getSampleParam() {
        if(samplerType == null || samplerType.isEmpty()) {
            return 1;
        } else {
            return sampleParam;
        }
    }

    public String getSamplerHostPort() {
        return samplerHostPort;
    }

    public Boolean getUseTraceId128Bit() {
        return useTraceId128Bit;
    }
}
