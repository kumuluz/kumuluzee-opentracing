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
import java.util.Optional;

/**
 * Zipkin config class
 *
 * @author Domen Kajdic
 * @author Domen Jeric
 * @since 1.0.0
 */
@ApplicationScoped
@ConfigBundle("kumuluzee.opentracing.zipkin")
public class ZipkinConfig {

    @ConfigValue("service-name")
    private String serviceName;

    @ConfigValue("agent-host")
    private String zipkinHost;

    @ConfigValue("tags")
    private String tags;

    @ConfigValue("sampler.type")
    private String samplerType;

    @ConfigValue("sampler.param")
    private Integer sampleParam;

    @ConfigValue("agent-port")
    private String samplerHostPort;

    @ConfigValue("traceid-128bit")
    private Boolean useTraceId128Bit;


    public String getServiceName() {
        return serviceName;
    }

    public String getZipkinHost() {
        return Optional.ofNullable(zipkinHost).orElse("http://localhost");
    }

    public String getTags() {
        return tags;
    }

    public String getSamplerType() {
        return Optional.ofNullable(samplerType).orElse("const");
    }

    public Integer getSampleParam() {
        return Optional.ofNullable(sampleParam).orElse(1);
    }

    public String getSamplerHostPort() {
        return Optional.ofNullable(samplerHostPort).orElse("9411");
    }

    public Boolean getUseTraceId128Bit() {
        return useTraceId128Bit;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setZipkinHost(String zipkinHost) {
        this.zipkinHost = zipkinHost;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setSamplerType(String samplerType) {
        this.samplerType = samplerType;
    }

    public void setSampleParam(Integer sampleParam) {
        this.sampleParam = sampleParam;
    }

    public void setSamplerHostPort(String samplerHostPort) {
        this.samplerHostPort = samplerHostPort;
    }

    public void setUseTraceId128Bit(Boolean useTraceId128Bit) {
        this.useTraceId128Bit = useTraceId128Bit;
    }
}
