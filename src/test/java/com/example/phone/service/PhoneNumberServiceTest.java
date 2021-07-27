package com.example.phone.service;

import com.example.phone.dto.PhoneDto;
import com.example.phone.mapper.PhoneMapper;
import com.example.phone.mapper.PhoneMapperImpl;
import com.example.phone.model.Phone;
import com.example.phone.repository.PhoneRepository;
import com.example.phone.util.TestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
class PhoneNumberServiceTest {
    private PhoneMapper phoneMapper = new PhoneMapperImpl();
    private PhoneNumberService phoneNumberService;
    @Mock
    private PhoneRepository phoneRepository;
    @Autowired
    private ObjectMapper mapper = new ObjectMapper();

    @BeforeAll
    public void setup() {
        MockitoAnnotations.initMocks(this);
        phoneNumberService = new PhoneNumberService(phoneRepository, phoneMapper);
    }

    @Test
    void findAll() throws Exception {
        String mockResponse = TestUtil.getFileContent("classpath:data/findAll_response.json");

        List<Phone> resultList =
                mapper.readValue(
                        mockResponse,
                        mapper.getTypeFactory().constructCollectionType(List.class, Phone.class));

        List<PhoneDto> expectedList =
                mapper.readValue(
                        mockResponse,
                        mapper.getTypeFactory().constructCollectionType(List.class, PhoneDto.class));

        when(phoneRepository.findAll())
                .thenReturn(Flux.fromIterable(resultList));

        StepVerifier.create(phoneNumberService.findAll())
                .expectNext(expectedList.get(0))
                .expectNext(expectedList.get(1))
                .expectNext(expectedList.get(2))
                .expectComplete()
                .verify();
    }

    @Test
    void findByCustomer() throws Exception{
        String mockResponse = TestUtil.getFileContent("classpath:data/findByCustomerId_response.json");

        List<Phone> resultList =
                mapper.readValue(
                        mockResponse,
                        mapper.getTypeFactory().constructCollectionType(List.class, Phone.class));

        List<PhoneDto> expectedList =
                mapper.readValue(
                        mockResponse,
                        mapper.getTypeFactory().constructCollectionType(List.class, PhoneDto.class));

        when(phoneRepository.findByCustomerId(12345L))
                .thenReturn(Flux.fromIterable(resultList));

        StepVerifier.create(phoneNumberService.findByCustomer(12345L))
                .expectNext(expectedList.get(0))
                .expectComplete()
                .verify();
    }

    @Test
    void activate() throws Exception{
        String mockResponse = TestUtil.getFileContent("classpath:data/activate_response.json");
        Phone response = mapper.readValue(mockResponse, Phone.class);
        PhoneDto expected = mapper.readValue(mockResponse, PhoneDto.class);

        when(phoneRepository.findByPhoneNumber("12345"))
                .thenReturn(Mono.just(response));
        response.setActivated(true);
        when(phoneRepository.save(response))
                .thenReturn(Mono.just(response));

        StepVerifier.create(phoneNumberService.activate("12345"))
                .expectNext(expected)
                .expectComplete()
                .verify();

    }
}