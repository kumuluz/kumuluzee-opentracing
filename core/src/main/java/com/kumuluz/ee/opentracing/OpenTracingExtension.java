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

import com.kumuluz.ee.common.Extension;
import com.kumuluz.ee.common.config.EeConfig;
import com.kumuluz.ee.common.dependencies.EeComponentDependencies;
import com.kumuluz.ee.common.dependencies.EeComponentDependency;
import com.kumuluz.ee.common.dependencies.EeComponentType;
import com.kumuluz.ee.common.dependencies.EeExtensionDef;
import com.kumuluz.ee.common.wrapper.KumuluzServerWrapper;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * KumuluzEE OpenTracing Extension
 * @author Domen Jeric
 * @since 1.0.0
 */

@EeExtensionDef(name = "OpenTracing", group = "OpenTracing")
@EeComponentDependencies({
    @EeComponentDependency(EeComponentType.JAX_RS),
    @EeComponentDependency(EeComponentType.CDI),
    @EeComponentDependency(EeComponentType.SERVLET)
})
public class OpenTracingExtension implements Extension {

    private static final Logger LOG = Logger.getLogger(OpenTracingExtension.class.getName());

    @Override
    public void init(KumuluzServerWrapper kumuluzServerWrapper, EeConfig eeConfig) {
        LOG.info("Initializing OpenTracing Extension.");
    }

    @Override
    public void load() {}

    @Override
    public List<String> scanLibraries() {
        return Collections.singletonList("kumuluzee-opentracing-core");
    }
}
