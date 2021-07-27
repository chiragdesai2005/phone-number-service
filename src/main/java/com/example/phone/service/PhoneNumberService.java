package com.example.phone.service;

import com.example.phone.dto.PhoneDto;
import com.example.phone.mapper.PhoneMapper;
import com.example.phone.repository.PhoneRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@Transactional
public class PhoneNumberService {

    private final PhoneRepository phoneRepository;
    private final PhoneMapper phoneMapper;

    public PhoneNumberService(PhoneRepository phoneRepository, PhoneMapper phoneMapper) {
        this.phoneRepository = phoneRepository;
        this.phoneMapper = phoneMapper;
    }

    /**
     * get all phone numbers
     */
    public Flux<PhoneDto> findAll() {
        return phoneRepository.findAll().map(phoneMapper::toDto);
    }

    /**
     * get all phone numbers of a single customer
     * @param id - customer id
     * @return
     */
    public Flux<PhoneDto> findByCustomer(final Long id) {
        return phoneRepository.findByCustomerId(id).map(phoneMapper::toDto);
    }

    /**
     * No validation added - simply update the activation flag for the given phone number
     * @param phoneNumber
     */
    public Mono<PhoneDto> activate(final String phoneNumber) {
        return phoneRepository.findByPhoneNumber(phoneNumber)
                .map(p -> {
                    log.info("Phone object={}",p.toString());
                    p.setActivated(true);
                    return p;
                })
                .flatMap(p -> this.phoneRepository.save(p))
                .map(phoneMapper::toDto);
    }
}
