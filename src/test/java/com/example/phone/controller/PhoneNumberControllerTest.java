package com.example.phone.controller;

import com.example.phone.config.WebFluxConfig;
import com.example.phone.dto.PhoneDto;
import com.example.phone.exception.ErrorResponse;
import com.example.phone.service.PhoneNumberService;
import com.example.phone.util.TestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.sleuth.autoconfig.brave.BraveAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.when;

@Import({BraveAutoConfiguration.class, WebFluxConfig.class})
@WebFluxTest(controllers = PhoneNumberController.class)
class PhoneNumberControllerTest {

    @Autowired
    WebTestClient webClient;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private PhoneNumberService phoneNumberService;


    @Test
    void findAll() throws Exception {
        String mockResponse = TestUtil.getFileContent("classpath:data/findAll_response.json");
        List<PhoneDto> resultList =
                mapper.readValue(
                        mockResponse,
                        mapper.getTypeFactory().constructCollectionType(List.class, PhoneDto.class));

        when(phoneNumberService.findAll())
                .thenReturn(Flux.fromIterable(resultList));
        webClient
                .get()
                .uri("/phone/api/v1/")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(PhoneDto.class)
                .isEqualTo(resultList);
    }

    @Test
    void findAllWithException(){
        when(phoneNumberService.findAll())
                .thenThrow(new RuntimeException("Test"));
        webClient
                .get()
                .uri("/phone/api/v1/")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .is5xxServerError()
                .expectBody(ErrorResponse.class);
    }


    @Test
    void findByCustomer() throws Exception {
        String mockResponse = TestUtil.getFileContent("classpath:data/findByCustomerId_response.json");
        List<PhoneDto> resultList =
                mapper.readValue(
                        mockResponse,
                        mapper.getTypeFactory().constructCollectionType(List.class, PhoneDto.class));

        when(phoneNumberService.findByCustomer(12345L))
                .thenReturn(Flux.fromIterable(resultList));
        webClient
                .get()
                .uri("/phone/api/v1/customer/12345")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(PhoneDto.class)
                .isEqualTo(resultList);
    }

    @Test
    void activate() throws Exception {
        String mockResponse = TestUtil.getFileContent("classpath:data/activate_response.json");
        PhoneDto response = mapper.readValue(mockResponse, PhoneDto.class);

        when(phoneNumberService.activate("0423181111"))
                .thenReturn(Mono.just(response));
        webClient
                .put()
                .uri("/phone/api/v1/activate/0423181111")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(PhoneDto.class)
                .isEqualTo(response);
    }
}