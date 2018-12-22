# KumuluzEE OpenTracing

[![Build Status](https://img.shields.io/travis/kumuluz/kumuluzee-opentracing/master.svg?style=flat)](https://travis-ci.org/kumuluz/kumuluzee-opentracing)

> KumuluzEE OpenTracing extension provides OpenTracing instrumentation for JAX-RS. 
Extension is compliant with MicroProfile OpenTracing specification.

KumuluzEE OpenTracing extension provides MicroProfile compliant distributed tracing solution for KumuluzEE framework. 



## Usage 
You can enable KumuluzEE OpenTracing extension by adding one of the following dependencies.

### Jaeger tracing
```xml
<dependency>
    <groupId>com.kumuluz.ee.opentracing</groupId>
    <artifactId>kumuluzee-opentracing-jaeger</artifactId>
    <version>${kumuluzee-opentracing.version}</version>
</dependency>
```

### Zipkin tracing
```xml
<dependency>
    <groupId>com.kumuluz.ee.opentracing</groupId>
    <artifactId>kumuluzee-opentracing-zipkin</artifactId>
    <version>${kumuluzee-opentracing.version}</version>
</dependency>
```

CDI, JAX-RS and Jetty servlet dependencies are prerequisites. 
Please refer to [KumuluzEE readme]( https://github.com/kumuluz/kumuluzee/) for more information.

## Configuration
Sample configuration file. For more information please refer to:
 [Jaeger client docs]( https://github.com/jaegertracing/jaeger-client-java/blob/master/jaeger-core/README.md ), 
 [Zipkin client docs](https://github.com/jaegertracing/jaeger-client-java/blob/master/jaeger-zipkin/README.md) and
 [MP-OT specification](https://github.com/eclipse/microprofile-opentracing/blob/master/spec/src/main/asciidoc/microprofile-opentracing.asciidoc).
```yaml
kumuluzee:
  opentracing:
    jaeger:
      service-name: KumuluzEE project # if not set kumuluzee.name value is used
      agent-host: localhost # default agent host
      agent-port: 5775 # default agent port
      endpoint: /api/traces
      auth-token: authToken
      username: username
      password: password
      reporter:
        log-spans: true
        max-queue-size: 10000
        flush-interval: 1000
      tags: key1=val1, key2=val2
      sampler: 
        type: const # default sampler type
        param: 1 # default sampler param
        manager-host-port: http://localhost:5775
      propagation: jaeger
      traceid-128bit: true
      
    zipkin:
      service-name: KumuluzEE project # if not set kumuluzee.name value is used
      agent-host: http://localhost # default agent host
      agent-port: 9411 # default agent port
      tags: key1=val1, key2=val2
      sampler:
        type: const # default sampler type
        param: 1 # default sampler param
      traceid-128bit: true

mp:
  opentracing:
    server:
      operation-name-provider: http-path
      skip-pattern: /openapi.*|/health.*
```

### Tracing with no code instrumentation
Tracing is automatically enabled by adding KumuluzEE OpenTracing extension dependency.

### Tracing with explicit code instrumentation
There is `@Traced` annotation available to define explicit Span creation. 
`@Traced` annotation can be added to class or method.\
Tracing in JAX-RS resource classes is enabled by default. 
It can be disabled by adding `@Traced(false)` annotation 
to class or method.

### Accessing configured tracer
The configured tracer object can be accessed by injecting Tracer class:
```java
@Inject
io.opentracing.Tracer configuredTracer;
```


## Tracing client requests
To enable JAX-RS client tracing, client filters should be 
added:
```java
Client httpClient = ClientTracingRegistrar.configure(ClientBuilder.newBuilder()).build();
```

For more in-depth specification and configuration options
please refer to [MicroProfile-OpenTracing (MP-OT)](https://github.com/eclipse/microprofile-opentracing).


## Changelog

Recent changes can viewed on Github on the [Releases Page](https://github.com/kumuluz/kumuluzee-opentracing/releases).


## Contribute

See the [contributing docs](https://github.com/kumuluz/kumuluzee-opentracing/blob/master/CONTRIBUTING.md).

When submitting an issue, please follow the 
[guidelines](https://github.com/kumuluz/kumuluzee-opentracing/blob/master/CONTRIBUTING.md#bugs).

When submitting a bugfix, write a test that exposes the bug and fails before applying your fix. Submit the test 
alongside the fix.

When submitting a new feature, add tests that cover the feature.

## License

MIT
