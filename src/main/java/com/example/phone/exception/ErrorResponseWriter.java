package com.example.phone.exception;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
public class ErrorResponseWriter {

  public static final ObjectMapper OBJECT_MAPPER =
      new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

  public static Mono<Void> writeErrorResponse(
      ServerWebExchange exchange, Throwable ex, String traceId) {
    final HttpStatus statusCode = getStatusCode(ex);
    final ServerHttpResponse response = exchange.getResponse();
    response.setStatusCode(statusCode);
    response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    ErrorResponse errorResponse =
        new ErrorResponse()
            .setStatus(statusCode.value())
            .setError(statusCode.getReasonPhrase())
            .setMessage(ex.getMessage())
            .setPath(exchange.getRequest().getPath().value())
            .setRequestId(traceId);
    return response.writeWith(getDataBuffer(exchange, errorResponse));
  }

  private static Mono<DataBuffer> getDataBuffer(
      ServerWebExchange exchange, ErrorResponse errorResponse) {
    return Mono.fromSupplier(
        () -> {
          DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
          try {
            return bufferFactory.wrap(OBJECT_MAPPER.writeValueAsBytes(errorResponse));
          } catch (JsonProcessingException e) {
            log.error("Error response write failed", e);
            return bufferFactory.wrap(new byte[0]);
          }
        });
  }

  // Map http status codes here
  public static HttpStatus getStatusCode(Throwable throwable) {
    if (throwable instanceof WebClientResponseException) {
      WebClientResponseException webClientResponseException =
          (WebClientResponseException) throwable;
      return webClientResponseException.getStatusCode();
    } else if (throwable instanceof ResponseStatusException) {
      ResponseStatusException responseStatusException = (ResponseStatusException) throwable;
      return responseStatusException.getStatus();
    }
    return INTERNAL_SERVER_ERROR;
  }
}
