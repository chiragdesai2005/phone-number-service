package com.example.phone.repository;


import com.example.phone.model.Phone;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
public interface PhoneRepository extends ReactiveCrudRepository<Phone, Long> {

    @Query("SELECT * FROM phone_numbers WHERE customer_id = :id")
    Flux<Phone> findByCustomerId(Long id);

    @Query("SELECT * FROM phone_numbers WHERE phone_number = :number")
    Mono<Phone> findByPhoneNumber(String number);
}
