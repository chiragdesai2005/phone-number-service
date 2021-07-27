package com.example.phone.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.sleuth.CurrentTraceContext;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.instrument.web.WebFluxSleuthOperators;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@Order(-2) // Should be before Webflux ResponseStatusExceptionHandler
@ConditionalOnProperty(
    name = "commons.error.handler.enabled",
    havingValue = "true",
    matchIfMissing = true)
public class GlobalErrorHandler implements ErrorWebExceptionHandler {

  @Autowired Tracer tracing;

  @Autowired CurrentTraceContext currentTraceContext;

  @Override
  public Mono<Void> handle(ServerWebExchange serverWebExchange, Throwable throwable) {
    Span span =
        WebFluxSleuthOperators.withSpanInScope(
            tracing,
            currentTraceContext,
            serverWebExchange,
            () -> {
              log.error("Exception being handled", throwable);
              return tracing.currentSpan();
            });

    return ErrorResponseWriter.writeErrorResponse(
        serverWebExchange, throwable, span.context().traceId());
  }
}
