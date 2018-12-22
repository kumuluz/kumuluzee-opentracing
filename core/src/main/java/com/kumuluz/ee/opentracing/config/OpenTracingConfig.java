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

import com.kumuluz.ee.common.runtime.EeRuntime;
import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.kumuluz.ee.opentracing.utils.CommonUtil;
import org.eclipse.microprofile.config.ConfigProvider;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * OpenTracing config
 * @author Domen Jeric
 * @author Domen Kajdic
 * @since 1.0.0
 */
@ApplicationScoped
public class OpenTracingConfig {

    private static final Logger LOG = Logger.getLogger(OpenTracingConfig.class.getName());
    private static final String MP_CONFIG_PREFIX = "mp.opentracing.";

    public String getServiceName(String serviceName) {
        if(serviceName == null || serviceName.isEmpty()) {
            serviceName = ConfigurationUtil.getInstance().get("kumuluzee.name").orElse(null);
        }
        if(serviceName == null) {
            LOG.severe("No service name provided. Using instance id.");
            serviceName = EeRuntime.getInstance().getInstanceId();
        }
        return serviceName;
    }

    public String getSelectedOperationNameProvider() {
        Optional<String> nameProvider = ConfigProvider.getConfig()
                .getOptionalValue(MP_CONFIG_PREFIX + "server.operation-name-provider", String.class);
        return nameProvider.orElse("class-method");
    }

    public Pattern getSkipPattern() {
        Optional<String> skipPattern = ConfigProvider.getConfig()
                .getOptionalValue(MP_CONFIG_PREFIX + "server.skip-pattern", String.class);
        return Pattern.compile(skipPattern.orElse("/health|/metrics.*|/openapi"));
    }
}
