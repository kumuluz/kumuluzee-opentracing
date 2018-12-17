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

package com.kumuluz.ee.opentracing.config;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.util.logging.Logger;

/**
 * Jaeger tracing config
 * @author Domen Jeric
 * @since 1.0.0
 */
@ApplicationScoped
public class JaegerTracingConfig implements OpenTracingConfigInterface {

    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 5775;
    private static final String CONFIG_PREFIX = "kumuluzee.opentracing.";

    private String reporterHost;
    private int reporterPort;

    private static final Logger LOG = Logger.getLogger(JaegerTracingConfig.class.getName());

    @PostConstruct
    public void init() {
        this.setReporterHost().setReporterPort();
        LOG.info(String.format("Jaeger config loaded: %s:%d", this.getReporterHost(), this.getReporterPort()));
    }

    @Override
    public String getReporterHost() {
        return reporterHost;
    }

    private JaegerTracingConfig setReporterHost() {
        this.reporterHost = ConfigurationUtil.getInstance()
                .get(CONFIG_PREFIX + "reporter-host")
                .orElse(DEFAULT_HOST);

        return this;
    }

    @Override
    public int getReporterPort() {
        return reporterPort;
    }

    private JaegerTracingConfig setReporterPort() {
        this.reporterPort =  ConfigurationUtil.getInstance()
                .getInteger(CONFIG_PREFIX + "reporter-port")
                .orElse(DEFAULT_PORT);

        return this;
    }
}
