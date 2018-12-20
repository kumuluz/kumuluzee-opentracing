package com.kumuluz.ee.opentracing.providers;

import com.kumuluz.ee.opentracing.filters.OpenTracingClientRequestFilter;
import com.kumuluz.ee.opentracing.filters.OpenTracingClientResponseFilter;
import io.opentracing.Tracer;
import io.opentracing.contrib.concurrent.TracedExecutorService;
import org.eclipse.microprofile.opentracing.ClientTracingRegistrarProvider;
import org.glassfish.jersey.client.JerseyClientBuilder;

import javax.enterprise.inject.spi.CDI;
import javax.ws.rs.client.ClientBuilder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientTracingProvider implements ClientTracingRegistrarProvider {
    @Override
    public ClientBuilder configure(ClientBuilder clientBuilder) {
        return configure(clientBuilder, Executors.newFixedThreadPool(10));
    }

    @Override
    public ClientBuilder configure(ClientBuilder clientBuilder, ExecutorService executorService) {
        Tracer tracer = CDI.current().select(Tracer.class).get();
        JerseyClientBuilder jerseyClientBuilder = (JerseyClientBuilder) clientBuilder;
        return jerseyClientBuilder
                .register(new OpenTracingClientRequestFilter(tracer))
                .register(new OpenTracingClientResponseFilter())
                .executorService(new TracedExecutorService(executorService, tracer));
    }
}
