package com.example.phone.controller;

import com.example.phone.dto.PhoneDto;
import com.example.phone.service.PhoneNumberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping(path = "${spring.webflux.base-path}")
/* This controller is responsible for phone numbers based API operations */
public class PhoneNumberController {

    private final PhoneNumberService phoneNumberService;

    public PhoneNumberController(PhoneNumberService phoneNumberService) {
        this.phoneNumberService = phoneNumberService;
    }

    /* get all phone numbers  */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<PhoneDto> findAll() {
        return phoneNumberService.findAll();
    }

    /* get all phone numbers of a single customer */
    @GetMapping(path = "/customer/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<PhoneDto> findByCustomer(@PathVariable final Long id) {
        return phoneNumberService.findByCustomer(id);
    }

    /* activate a phone number */
    @PutMapping(path = "/activate/{phoneNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<PhoneDto> activate(@PathVariable final String phoneNumber) {
        return phoneNumberService.activate(phoneNumber);
    }

}
